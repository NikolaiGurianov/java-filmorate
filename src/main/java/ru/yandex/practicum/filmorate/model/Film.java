package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;

    @NotNull
    @NotBlank(message = "Название фильма не может состоять только из пробелов")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Min(value = 0, message = "Продолжительность фильма должна быть положительной")
    private Integer duration;

    @JsonIgnore
    private final Set<Integer> likedByUsers = new HashSet<>();

    @JsonIgnore
    private int like = 0;

    public Film(int id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(int userId) {
        if (likedByUsers.add(userId)) {
            like++;
        }
    }

    public void removeLike(int userId) {
        if (likedByUsers.remove(userId)) {
            like--;
        }
    }


}
