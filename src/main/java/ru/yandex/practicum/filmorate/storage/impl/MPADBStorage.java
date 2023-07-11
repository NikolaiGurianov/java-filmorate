package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.MPARowMapper;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MPADBStorage implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public MPA getMPAById(Integer id) {
        try {
            log.debug("Получен рейтинг по id {}.", id);
            return jdbcTemplate.queryForObject("select * from mpa where id = ?", new MPARowMapper(), id);
        } catch (Exception e) {
            throw new NotFoundException("Такой ID рейтинга не найден.");
        }
    }

    @Override
    public List<MPA> getAllMpa() {
        log.debug("Cписок MPA получен");
        return jdbcTemplate.query("select * from mpa order by id asc", new MPARowMapper());
    }
}
