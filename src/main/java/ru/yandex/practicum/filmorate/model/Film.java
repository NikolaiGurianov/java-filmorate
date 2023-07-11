package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;
    @NotNull
    @NotBlank(message = "Название фильма не может состоять только из пробелов")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private Integer duration;
    @NotNull(message = "Должен быть указан жанр фильма")
    private Set<Genre> genres = new HashSet<>();
    @NotNull(message = "Должен быть указан рейтинг MPA")
    private MPA mpa;
    @JsonIgnore
    private final Set<Integer> likedByUsers = new HashSet<>();
    private Integer rate;
}
