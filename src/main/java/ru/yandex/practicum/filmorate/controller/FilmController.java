package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int generatedFilmId = 1;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(generatedFilmId++);
        filmMap.put(film.getId(), film);
        log.debug("Фильм создан: '{}'", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updateFilm) {
        if (updateFilm != null && filmMap.containsKey(updateFilm.getId())) {
            filmMap.put(updateFilm.getId(), updateFilm);
            log.debug("Фильм обновлен: '{}'", updateFilm);
            return updateFilm;
        } else {
            throw new ValidationException("Фильм не обновлен. Не найден id фильма.");
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Фильм не создан. Не указано название фильма.");
        }
        if (film.getDuration() == null || film.getDuration() < 0) {
            throw new ValidationException("Фильм не создан. Некорректная продолжительность фильма.");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("Фильм не создан. Некорректное описание фильма.");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Фильм не создан. Некорректная дата выпуска фильма.");
        }
    }

}
