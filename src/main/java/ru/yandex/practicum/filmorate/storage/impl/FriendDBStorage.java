package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendDBStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;


    @Override
    public void addFriend(int userId, int friendId) {
        if (!userExists(userId) || !userExists(friendId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        boolean friendshipExists = jdbcTemplate.queryForObject(
                "select count(*) from friends where (user_id = ? and friend_id = ?)" +
                        " or (user_id = ? and friend_id = ?)",
                Integer.class, userId, friendId, friendId, userId
        ) > 1;

        if (friendshipExists) {
            throw new ValidationException("Пользователи уже являются друзьями.");
        }

        boolean requestExists = jdbcTemplate.queryForObject(
                "select count(*) from friends where (user_id = ? and friend_id = ?)" +
                        " or (user_id = ? and friend_id = ?)",
                Integer.class, userId, friendId, friendId, userId
        ) > 0;

        if (requestExists) {
            throw new ValidationException("Запрос на добавление в друзья уже отправлен или получен.");
        }
        jdbcTemplate.update("insert into friends (user_id, friend_id) values (?, ?)", userId, friendId);
        log.debug("Запрос на добавление в друзья отправлен от пользователя с ID {} к пользователю с ID {}.", userId, friendId);
    }


    @Override
    public void deleteFriend(int userId, int friendId) {
        if (!userExists(userId) || !userExists(friendId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        jdbcTemplate.update("delete from friends where (user_id = ? and friend_id = ?)" +
                        " or (user_id = ? and friend_id = ?)",
                userId, friendId, friendId, userId);
        log.info("Дружба между пользователями с ID {} и {} удалена.", userId, friendId);
    }

    public Set<User> getFriendsById(int userId) {
        if (!userExists(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        List<User> friends = jdbcTemplate.query(
                "select u.* from users u " +
                        "join friends f on f.friend_id = u.id " +
                        "where f.user_id = ? order by u.id asc",
                new UserRowMapper(), userId
        );

        return new HashSet<>(friends);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        if (!userExists(userId) || !userExists(friendId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        List<User> mutualFriends = new ArrayList<>(getFriendsById(userId));
        mutualFriends.retainAll(getFriendsById(friendId));

        return mutualFriends;
    }


    private boolean userExists(int userId) {
        return userStorage.getUserById(userId) != null;
    }

}
