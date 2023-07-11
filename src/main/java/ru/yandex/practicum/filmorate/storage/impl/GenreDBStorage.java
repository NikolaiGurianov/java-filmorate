package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDBStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Integer id) {
        try {
            log.debug("Получен жанр по id {}.", id);
            return jdbcTemplate.queryForObject("select * from genres where id =?", new GenreRowMapper(), id);
        } catch (Exception e) {
            throw new NotFoundException("Такой ID жанра не найден.");
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        log.debug("Cписок жанров получен");
        return jdbcTemplate.query("select * from genres order by id asc", new GenreRowMapper());
    }

    @Override
    public List<Genre> getGenresForFilm(int filmId) {
        return jdbcTemplate.query(
                "SELECT g.id, g.name from genres g  join genres_films gf on g.id = gf.genre_id " +
                        "where gf.film_id = ? order by genre_id asc",
                new GenreRowMapper(), filmId);
    }

    @Override
    public void updateGenres(Film film) {
        jdbcTemplate.update("delete from genres_films where film_id=?", film.getId());

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("insert into genres_films (film_id, genre_id) values (?,?)", film.getId(), genre.getId());
        }
        film.setGenres(new HashSet<>(getGenresForFilm(film.getId())));
    }
}
