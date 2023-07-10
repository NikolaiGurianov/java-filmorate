package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;


    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        if (user != null) {
            return userStorage.createUser(user);
        } else {
            throw new ValidationException("Данные пользователя пусты");
        }
    }

    public User updateUser(User updatedUser) {
        if (userStorage.getUserById(updatedUser.getId()) != null) {
            return userStorage.updateUser(updatedUser);
        } else
            throw new NotFoundException("Пользователь не обновлен. Не найден пользователь с ID " + updatedUser.getId());
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        if (getUserById(userId) != null || getUserById(friendId) != null) {
            friendStorage.addFriend(userId, friendId);
        } else {
            throw new NotFoundException("Пользователи не найдены");
        }
    }

    public void deleteFriend(int userId, int friendId) {
        if (getUserById(userId) != null || getUserById(friendId) != null) {
            friendStorage.deleteFriend(userId, friendId);
        } else {
            throw new NotFoundException("Пользователи не найдены или уже удалены");
        }
    }

    public Set<User> getFriendsById(int userId) {
        if (userStorage.getUserById(userId) != null) {
            return friendStorage.getFriendsById(userId);
        } else {
            throw new NotFoundException("Пользователь не обновлен. Не найден пользователь с ID " + userId);
        }
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        if (getUserById(userId) != null || getUserById(friendId) != null) {
            return friendStorage.getCommonFriends(userId, friendId);
        } else {
            throw new NotFoundException("Пользователи не найдены");
        }
    }
}
