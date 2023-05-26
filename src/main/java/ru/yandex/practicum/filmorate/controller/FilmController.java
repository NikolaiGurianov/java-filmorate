package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
    private int generatedFilmId = 1;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validateFilm(film);
        film.setId(generatedFilmId++);
        filmMap.put(film.getId(), film);
        log.debug("Фильм создан: '{}'", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updateFilm) {
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
        if (film.getName().isBlank() || film.getDuration() < 0 || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
        ) {
            throw new ValidationException("Фильм не создан, не прошел проверку");
        }
    }

}
