package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private final Validator validator = new Validator();
    private int id = 1;

    @Override
    public Film createFilm(Film film) {
        validator.validateFilm(film);
        film.setId(id++);
        filmMap.put(film.getId(), film);
        log.debug("Фильм создан: '{}'", film);
        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        validator.validateFilm(updatedFilm);
        if (filmMap.containsKey(updatedFilm.getId())) {
            filmMap.put(updatedFilm.getId(), updatedFilm);
            log.debug("Фильм обновлен: '{}'", updatedFilm);
            return updatedFilm;
        } else {
            throw new NotFoundException("Фильм не обновлен. Не найден id фильма.");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("Колличество фильмов: '{}'", filmMap.size());
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film getFilmById(int id) {
        if (filmMap.containsKey(id)) {
            return filmMap.get(id);
        } else {
            throw new NotFoundException("Фильм с таким ID не найден");
        }
    }
}
