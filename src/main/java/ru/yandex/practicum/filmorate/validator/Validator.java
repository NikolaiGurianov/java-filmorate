package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validator {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void validateFilm(Film film) {
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

    public void validateUser(User user) {
        LocalDate now = LocalDate.now();
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Пользователь не создан. Некорректная электронная почта.");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Пользователь не создан. Некорректный логин.");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(now)) {
            throw new ValidationException("Пользователь не создан. Некорректная дата рождения.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
