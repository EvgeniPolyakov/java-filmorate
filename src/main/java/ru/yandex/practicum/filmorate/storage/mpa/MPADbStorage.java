package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MPADbStorage implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPARating getMPAById(Long id) {
        String sql = "SELECT * FROM MPA_RATING WHERE RATING_ID = ?";
        return jdbcTemplate.query(sql, this::makeMPA, id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг MPA c id %s не найден.", id)));
    }

    @Override
    public List<MPARating> getMPAValues() {
        String sql = "SELECT * FROM MPA_RATING";
        return jdbcTemplate.query(sql, this::makeMPA);
    }

    private MPARating makeMPA(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("RATING_ID");
        String rating = rs.getString("RATING_VALUE");
        return new MPARating(id, rating);
    }
}