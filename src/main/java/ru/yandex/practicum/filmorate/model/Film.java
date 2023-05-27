package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

@lombok.AllArgsConstructor
@lombok.Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
