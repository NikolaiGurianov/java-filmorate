package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос к эндпоинту: '/users' на создание нового пользователя");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User updatedUser) {
        log.info("Получен PUT-запрос к эндпоинту: '/users' на обновление пользователя");
        return userService.updateUser(updatedUser);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получен GET-запрос к эндпоинту: '/users' на получение всех пользователей");
        return userService.getAllUsers();
    }


    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен PUT-запрос к эндпоинту: '/users/{id}/friends/{friendId}' на добавление друга");
        userService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{id}/friends' на получение всех друзей пользователя");
        return userService.getFriendsById(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен GET-запрос к эндпоинту: '{id}/friends/common/{otherId}' на получение " +
                "всех общих друзей пользователей");
        return userService.getCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен DELETE-запрос к эндпоинту: '/users//{id}/friends/{friendId}' " +
                "на удаление из дружбы пользователя");
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту: '/users' на получение жанра с ID={}", id);
        return userService.getUserById(id);
    }
}
