package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
@Primary
public class UserDBStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());
        params.put("email", user.getEmail());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        log.info("Добавлен новый пользователь с ID={}", user.getId());
        return user;
    }


    @Override
    public User updateUser(User updatedUser) {
        jdbcTemplate.update("update users SET " +
                        "login = ?, name = ?, birthday = ?, email = ? " +
                        "WHERE id = ?",
                updatedUser.getLogin(),
                updatedUser.getName(),
                updatedUser.getBirthday(),
                updatedUser.getEmail(),
                updatedUser.getId());
        log.debug("Пользователь обновлен: '{}'", updatedUser);
        return updatedUser;

    }


    @Override
    public List<User> getAllUsers() {
        List<User> users = jdbcTemplate.query("select * from users", this::mapRowToUser);
        log.debug("Колличество пользователей: '{}'", users.size());
        return users;
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                (rs.getInt("id")), (rs.getString("email")), (rs.getString("login")),
                (rs.getString("name")), (rs.getDate("birthday").toLocalDate()),
                (getFriends(rs.getInt("id"))));
    }

    private Set<Integer> getFriends(int userId) {
        List<Integer> friends =
                jdbcTemplate.query("select friend_id from friends where user_id=?",
                        (rs, rowNum) -> rs.getInt("friend_id"), userId);
        log.debug("Колличество друзей пользователя: '{}'", friends.size());
        return new HashSet<>(friends);
    }

    @Override
    public User getUserById(int id) {
        try {
            return jdbcTemplate.queryForObject("select * from users where id =?", new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с таким ID не найден");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Возникла ошибка при получении пользователя");
        }
    }
}



