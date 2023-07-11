package ru.yandex.practicum.filmorate.storage;


import java.util.Set;

public interface LikeStorage {
    void deleteLike(Integer filmId, Integer userId);

    void addLike(Integer filmId, Integer userId);

    Set<Integer> getLikesFilmId(Integer filmId);

}
