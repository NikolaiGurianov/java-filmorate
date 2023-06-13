package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
    }

    @Test
    void createTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6), 169);
        Film expectedFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6), 169);

        Film resultFilm = filmController.createFilm(actualFilm);
        Assertions.assertEquals(expectedFilm, resultFilm);
    }

    @Test
    void createFailDescriptionTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "Наше время на Земле подошло к концу, " +
                "команда исследователей берет на себя самую важную миссию в истории человечества; путешествуя за " +
                "пределами нашей галактики, чтобы узнать есть ли у человечества будущ",
                LocalDate.of(2014, 11, 6), 169);
        assertEquals(201, actualFilm.getDescription().length());
        assertThrows(ValidationException.class, () -> filmController.createFilm(actualFilm));
    }

    @Test
    void createFailReleaseDateTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh",
                LocalDate.of(1895, 12, 27), 169);
        assertThrows(ValidationException.class, () -> filmController.createFilm(actualFilm));
    }

    @Test
    void createFailDurationTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh",
                LocalDate.of(2014, 11, 6), -169);
        assertThrows(ValidationException.class, () -> filmController.createFilm(actualFilm));
    }
}
