package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreStorage genreStorage;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        log.info("Получен GET-запрос к эндпоинту: '/genres' на получение всех жанров");
        return genreStorage.getAllGenre();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable @Valid Integer id) {
        log.info("Получен GET-запрос к эндпоинту: '/genres' на получение жанра с ID={}", id);
        return genreStorage.getGenreById(id);
    }
}

