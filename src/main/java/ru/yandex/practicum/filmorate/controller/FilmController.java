package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен POST-запрос к эндпоинту: '/films' на создание нового фильма");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updateFilm) {
        log.info("Получен PUT-запрос к эндпоинту: '/films' на обновление фильма");
        return filmService.updateFilm(updateFilm);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен GET-запрос к эндпоинту: '/films' на получение всех фильмов");
        return filmService.getAllFilms();
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен PUT-запрос к эндпоинту: '/users/{id}/like/{userId}' на добавление в список понравившихся");
        filmService.likeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        log.info("Получен PUT-запрос к эндпоинту: '/users/{id}/friends/{friendId}' на добавление друга");
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен DELETE-запрос к эндпоинту: '/users/{id}/like/{userId}' на удаления из списка понравившихся");
        filmService.removeLikeFilm(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту: '/films/{id}' на получение фильма с ID={}", id);
        return filmService.getFilmById(id);
    }
}
