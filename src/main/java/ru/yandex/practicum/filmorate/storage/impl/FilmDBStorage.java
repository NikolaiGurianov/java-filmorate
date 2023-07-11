package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;
import ru.yandex.practicum.filmorate.storage.model.FilmDB;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
@Primary
public class FilmDBStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpa_id", film.getMpa().getId());
        params.put("rate", film.getRate());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());
        film.setMpa(mpaStorage.getMPAById(film.getMpa().getId()));
        genreStorage.updateGenres(film);
        log.info("Добавлен новый фильм с ID={}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        int updateCount = jdbcTemplate.update("update films set name = ?, description = ?, release_date = ?, " +
                        "duration = ?, mpa_id = ?, rate = ? where id = ?", updatedFilm.getName(),
                updatedFilm.getDescription(), updatedFilm.getReleaseDate(), updatedFilm.getDuration(),
                updatedFilm.getMpa().getId(), updatedFilm.getRate(), updatedFilm.getId());
        if (updateCount != 0) {
            updatedFilm.setMpa(mpaStorage.getMPAById(updatedFilm.getMpa().getId()));
            genreStorage.updateGenres(updatedFilm);
            log.debug("Фильм обновлен: '{}'", updatedFilm);
            return updatedFilm;
        } else {
            throw new NotFoundException("Фильм не обновлен. Не найден id фильма.");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<FilmDB> filmDBList = jdbcTemplate.query("select* from films ", new FilmRowMapper());
        return filmDBList.stream()
                .map(this::fromDBToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(int id) {
        FilmDB filmDB = jdbcTemplate.queryForObject("select * from films where id = ?",
                new FilmRowMapper(), id);
        return fromDBToDto(Objects.requireNonNull(filmDB));
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