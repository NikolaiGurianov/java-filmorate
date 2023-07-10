package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.model.FilmDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<FilmDB> {
    @Override
    public FilmDB mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmDB filmDB = new FilmDB();
        filmDB.setId(rs.getInt("id"));
        filmDB.setName(rs.getString("name"));
        filmDB.setDescription(rs.getString("description"));
        filmDB.setReleaseDate(rs.getDate("release_date").toLocalDate());
        filmDB.setDuration(rs.getInt("duration"));
        filmDB.setMpaId(rs.getInt("mpa_id"));
        filmDB.setRate(rs.getInt("rate"));
        return filmDB;
    }
}
