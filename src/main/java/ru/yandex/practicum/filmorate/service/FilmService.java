package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void likeFilm(int filmId, int userId) {
        if (userId <= 0) {
            log.info("Недопустимое значение ID пользователя");
            throw new NotFoundException("Недопустимое значение ID пользователя");
        }
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(filmId);
        log.info("Фильм добавлен в список понравившихся");
    }

    public void removeLikeFilm(int filmId, int userId) {
        if (userId <= 0) {
            log.info("Недопустимое значение ID пользователя");
            throw new NotFoundException("Недопустимое значение ID пользователя");
        }
        Film film = filmStorage.getFilmById(filmId);
        film.removeLike(filmId);
        log.info("Фильм удален из списка понравившихся");
    }

    public List<Film> getPopularFilms(int limit) {
        List<Film> popularFilms = new ArrayList<>(filmStorage.getAllFilms());
        return popularFilms.stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }
}
