package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();

    @Test
    void createTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2));

        User expectedUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2));

        User resultUser = userController.createUser(actualUser);
        Assertions.assertEquals(expectedUser, resultUser);
    }

    @Test
    void createEmptyNameTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "Tyler",
                LocalDate.of(2000, 1, 2));

        User expectedUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2));
        expectedUser.setName("");

        User resultUser = userController.createUser(expectedUser);
        Assertions.assertEquals(actualUser, resultUser);
    }

    @Test
    void createInvalidEmailTest() {
        User actualUser = new User(1, "yandex/yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2));
        assertThrows(ValidationException.class, () -> userController.createUser(actualUser));
    }

    @Test
    void createInvalidLoginTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "The Creator", "The Creator",
                LocalDate.of(2000, 1, 2));
        assertThrows(ValidationException.class, () -> userController.createUser(actualUser));
    }

    @Test
    void createBirthdayLoginTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(9999, 1, 2));
        assertThrows(ValidationException.class, () -> userController.createUser(actualUser));
    }
}