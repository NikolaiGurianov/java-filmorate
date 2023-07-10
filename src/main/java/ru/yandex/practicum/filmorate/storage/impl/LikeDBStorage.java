package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.model.FilmDB;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDBStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;


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

    @Override
    public List<Film> getPopularFilms(int limit) {
        return jdbcTemplate.query("select f.*, count(l.user_id) as rate from films f " +
                                "left join likes l on f.id = l.film_id group by f.id order by rate DESC limit ?",
                        new FilmRowMapper(), limit).stream()
                .map(this::fromDBToDto)
                .collect(Collectors.toList());
    }

    private Film fromDBToDto(FilmDB filmDB) {
        Film film = new Film();
        film.setId(filmDB.getId());
        film.setName(filmDB.getName());
        film.setDescription(filmDB.getDescription());
        film.setDuration(filmDB.getDuration());
        film.setReleaseDate(filmDB.getReleaseDate());
        film.setMpa(mpaStorage.getMPAById(filmDB.getMpaId()));
        film.setGenres(new HashSet<>(genreStorage.getGenresForFilm(filmDB.getId())));
        film.setRate(filmDB.getRate());
        return film;
    }
}
