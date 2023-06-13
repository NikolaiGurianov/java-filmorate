package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public ResponseEntity<String> likeFilm(int filmId, int userId) {
        if (userId <= 0) {
            log.info("Недопустимое значение ID пользователя");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Недопустимое значение ID пользователя");
        }
        Film film = filmStorage.getFilmById(filmId);
        film.getLikedByUsers().add(userId);
        film.setLike(film.getLike() + 1);
        filmStorage.updateFilm(film);
        log.info("Фильм добавлен в список понравившихся");
        return ResponseEntity.ok("Фильм добавлен в список понравившихся");
    }

    public ResponseEntity<String> removeLikeFilm(int filmId, int userId) {
        if (userId <= 0) {
            log.info("Недопустимое значение ID пользователя");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Недопустимое значение ID пользователя");
        }
        Film film = filmStorage.getFilmById(filmId);
        film.getLikedByUsers().remove(userId);
        film.setLike(film.getLike() - 1);
        filmStorage.updateFilm(film);
        log.info("Фильм удален из списка понравившихся");
        return ResponseEntity.ok("Фильм удален из списка понравившихся");
    }

    public List<Film> getPopularFilms() {
        List<Film> popularFilms = new ArrayList<>(filmStorage.getAllFilms());
        return popularFilms.stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .limit(10)
                .collect(Collectors.toList());
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
