package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final Validator validator = new Validator();


    public Film updateFilm(Film film) {
        validator.validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public Film createFilm(Film film) {
        validator.validateFilm(film);
        return filmStorage.createFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        try {
            return filmStorage.getFilmById(id);
        } catch (Exception e) {
        throw new NotFoundException("Фильм с таким ID не найден");
    }
    }

    public void likeFilm(int filmId, int userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void removeLikeFilm(int filmId, int userId) {
        if (filmId < 1 || userId < 1) {
            throw new NotFoundException("ID пользователя или фильма не может быть меньше 1.");
        }
        likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int limit) {
        return filmStorage.getPopularFilms(limit);
    }
}
