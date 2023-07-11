package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDBStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        int deletedRows = jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", filmId, userId);
        if (deletedRows == 0) {
            throw new NotFoundException("Лайк не найден. Не удалось удалить лайк.");
        }
        int updatedRows = jdbcTemplate.update("update films set rate = rate - 1 where id = ? and rate > 0", filmId);
        if (updatedRows == 0) {
            throw new NotFoundException("Фильм с таким ID не найден или счетчик лайков уже равен нулю.");
        }
        log.debug("Из списка понравившихся фильмов с ID {} удален лайк пользователя с ID {}. Счетчик лайков уменьшен на 1.", filmId, userId);
    }


    @Override
    public void addLike(Integer filmId, Integer userId) {
        int updatedRows = jdbcTemplate.update("update films set rate = rate + 1 where id = ?", filmId);
        if (updatedRows == 0) {
            throw new NotFoundException("Фильм с таким ID не найден");
        }

        jdbcTemplate.update("insert into likes(film_id, user_id) values (?, ?)", filmId, userId);
        log.debug("Фильм с ID {} понравился пользователю с ID {}. Счетчик лайков увеличен на 1.", filmId, userId);
    }


    @Override
    public Set<Integer> getLikesFilmId(Integer filmId) {
        List<Integer> usersId = jdbcTemplate.query("select user_id from likes where film_id = ?",
                (rs, rowNum) -> rs.getInt("user_id"), filmId);
        log.debug("Получен список user_id понравившимся Film с id {}.", filmId);
        return new HashSet<>(usersId);
    }
}
