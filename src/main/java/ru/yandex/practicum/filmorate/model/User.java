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
public class User {
    private int id;

    @Email(message = "Неверный формат электронной почты")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private String email;

    @NotBlank(message = "Логин пользователя не может быть пустым")
    @Pattern(regexp = "\\S+", message = "В логине не могут находиться пробелы")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения пользователя должна быть указана")
    @Past(message = "Дата рождения пользователя должна быть в прошлом")
    private LocalDate birthday;

    @JsonIgnore
    private Set<Integer> friends = new HashSet<>();

}

