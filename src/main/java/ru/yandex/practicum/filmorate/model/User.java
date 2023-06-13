package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    private int id;

    @Email(message = "Неверный формат электронной почты")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private String email;

    @NotBlank(message = "Логин пользователя не может быть пустым")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения пользователя должна быть указана")
    private LocalDate birthday;

    private final Set<Integer> friends = new HashSet<>();
    private final Set<Integer> likedFilms = new HashSet<>();

}

