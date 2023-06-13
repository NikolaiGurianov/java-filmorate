package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updateFilm) {
        return filmService.updateFilm(updateFilm);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<String> addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.likeFilm(id, userId);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopular(@RequestParam(value = "count", required = false) Integer count) {
        List<Film> result;
        if (count != null) {
            result = filmService.getPopularFilms(count);
        } else {
            result = filmService.getPopularFilms();
        }
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> removeLikeFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.removeLikeFilm(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }


}
