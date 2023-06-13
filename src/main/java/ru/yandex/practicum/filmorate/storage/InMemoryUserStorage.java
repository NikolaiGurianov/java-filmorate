package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap = new HashMap<>();
    private final Validator validator = new Validator();
    private int id = 1;

    @Override
    public User createUser(User user) {
        validator.validateUser(user);
        user.setId(id++);
        userMap.put(user.getId(), user);
        log.debug("Пользователь создан: '{}'", user);
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        validator.validateUser(updatedUser);
        if (userMap.containsKey(updatedUser.getId())) {
            userMap.put(updatedUser.getId(), updatedUser);
            log.debug("Пользователь обновлен: '{}'", updatedUser);
            return updatedUser;
        } else {
            throw new ValidationException("Пользователь не обновлен. Не найден id пользователя.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Колличество пользователей: '{}'", userMap.size());
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUserById(int id) {
        if (userMap.containsKey(id)) {
            return userMap.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с таким ID не найден");
        }
    }
}
