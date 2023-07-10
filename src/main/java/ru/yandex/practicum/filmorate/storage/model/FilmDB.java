package ru.yandex.practicum.filmorate.storage.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FilmDB {

    private Integer id;
    private String name;
    private String description;
    private Integer duration;
    private Integer mpaId;
    private Integer genreId;
    private LocalDate releaseDate;
    private Integer rate;

}

