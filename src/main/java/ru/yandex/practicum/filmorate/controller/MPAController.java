package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPAController {
    private final MPAStorage mpaStorage;

    @GetMapping
    public Collection<MPA> getAllMpa() {
        log.info("Получен GET-запрос к эндпоинту: '/mpa' на получение всех рейтингов");
        return mpaStorage.getAllMpa();
    }

    @GetMapping("/{id}")
    public MPA getMpaById(@PathVariable Integer id) {
        log.info("Получен GET-запрос к эндпоинту: '/mpa' на получение жанра с ID={}", id);
        return mpaStorage.getMPAById(id);
    }
}
