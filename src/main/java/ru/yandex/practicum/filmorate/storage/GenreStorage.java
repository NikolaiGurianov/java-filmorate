package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenreById(Integer id);

    List<Genre> getAllGenre();

    List<Genre> getGenresForFilm(int filmId);

    void updateGenres(Film film);
}
