package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int generatedUserId = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateUser(user);
        user.setId(generatedUserId++);
        userMap.put(user.getId(), user);
        log.debug("Пользователь создан: '{}'", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User updatedUser) {
        if (updatedUser != null && userMap.containsKey(updatedUser.getId())) {
            userMap.put(updatedUser.getId(), updatedUser);
            log.debug("Пользователь обновлен: '{}'", updatedUser);
            return updatedUser;
        } else {
            throw new ValidationException("Пользователь не обновлен. Не найден id пользователя.");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    private void validateUser(User user) {
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
