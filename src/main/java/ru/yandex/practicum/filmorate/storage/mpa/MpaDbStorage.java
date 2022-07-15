package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String RATING_ID_FIELD = "RATING_ID";
    private static final String RATING_VALUE_FIELD = "RATING_VALUE";

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getMpaById(Long id) {
        String sql = "SELECT * FROM MPA_RATING WHERE RATING_ID = ?";
        return jdbcTemplate.query(sql, this::makeMpa, id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг MPA c id %s не найден.", id)));
    }

    @Override
    public List<MpaRating> getMpaValues() {
        String sql = "SELECT * FROM MPA_RATING";
        return jdbcTemplate.query(sql, this::makeMpa);
    }

    private MpaRating makeMpa(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong(RATING_ID_FIELD);
        String rating = rs.getString(RATING_VALUE_FIELD);
        return new MpaRating(id, rating);
    }
}