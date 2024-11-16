package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mapper;

    @Override
    public Optional<Mpa> getMpaById(Long id) {
        String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, mapper, id));
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM mpa ORDER BY mpa_id";
        return jdbcTemplate.query(sqlQuery, mapper);
    }

    @Override
    public boolean containsMpa(Long mpaId) {
        String sqlQuery = "SELECT 1 FROM mpa WHERE mpa_id = ? LIMIT 1";
        try {
            jdbcTemplate.queryForObject(sqlQuery, Boolean.class, mpaId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
