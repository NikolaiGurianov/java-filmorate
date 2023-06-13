package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

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

    public ResponseEntity<String> addFriend(int userId, int friendId) {
        if (isFriend(userId, friendId)) {
            log.info("Пользователи уже друзья");
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Пользователи уже друзья");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return ResponseEntity.ok("Пользователи добавлены в друзья");
    }

    public ResponseEntity<String> deleteFriend(int userId, int friendId) {
        if (!isFriend(userId, friendId)) {
            log.info("Пользователи не были добавлены в друзья");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователи не были добавлены в друзья");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(userId);
        friend.getFriends().remove(userId);
        return ResponseEntity.ok("Пользователи удалены из друзей");
    }

    public List<User> getFriendsById(int userId) {
        Set<Integer> friends = userStorage.getUserById(userId).getFriends();
        if (friends.isEmpty()) {
            throw new ValidationException("Список друзей пуст");
        }
        List<User> result = new ArrayList<>();
        for (Integer friendId : friends) {
            User friend = userStorage.getUserById(friendId);
            result.add(friend);
        }
        return result;
    }

    public List<User> getMutualFriend(int userId, int friendId) {
        Set<Integer> userFriends = userStorage.getUserById(userId).getFriends();
        Set<Integer> friendFriends = userStorage.getUserById(friendId).getFriends();
        List<User> mutualFriends = new ArrayList<>();

        if (userFriends.isEmpty() || friendFriends.isEmpty()) {
            log.info("У одного из пользователей упстой список друзей");
            return mutualFriends;
        }
        Set<Integer> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(friendFriends);

        if (commonFriends.isEmpty()) {
            log.info("У пользователей нет общих друзей");
            return mutualFriends;
        }
        for (Integer integer : commonFriends) {
            User friend = userStorage.getUserById(integer);
            mutualFriends.add(friend);
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
