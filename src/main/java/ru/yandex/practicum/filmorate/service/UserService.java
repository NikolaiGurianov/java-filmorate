package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        if (isFriend(userId, friendId)) {
            log.info("Пользователи уже друзья");
            throw new ValidationException("Пользователи уже друзья");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователи добавлены в друзья");
    }

    public void deleteFriend(int userId, int friendId) {
        if (!isFriend(userId, friendId)) {
            log.info("Пользователи не были добавлены в друзья");
            throw new NotFoundException("Пользователи не были добавлены в друзья");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователи удалены из друзей");
    }

    public List<User> getFriendsById(int userId) {
        Set<Integer> friends = userStorage.getUserById(userId).getFriends();
        if (friends.isEmpty()) {
            throw new ValidationException("Список друзей пуст");
        }
        return friends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriend(int userId, int friendId) {
        Set<Integer> userFriends = userStorage.getUserById(userId).getFriends();
        Set<Integer> friendFriends = userStorage.getUserById(friendId).getFriends();
        List<User> mutualFriends = new ArrayList<>();

        if (userFriends.isEmpty() || friendFriends.isEmpty()) {
            log.info("У одного из пользователей пустой список друзей");
            return mutualFriends;
        }
        mutualFriends = userFriends.stream()
                .filter(friendFriends::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());

        if (mutualFriends.isEmpty()) {
            log.info("У пользователей нет общих друзей");
        }
        return mutualFriends;
    }

    public boolean isFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        return ((user != null && user.getFriends().contains(friendId)) || (friend != null &&
                friend.getFriends().contains(friendId)));
    }
}
