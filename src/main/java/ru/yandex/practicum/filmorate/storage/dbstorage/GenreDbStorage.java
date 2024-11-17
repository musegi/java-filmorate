package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper mapper;

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "SELECT * FROM genres ORDER BY genre_id";
        return jdbcTemplate.query(sqlQuery, mapper);
    }

    @Override
    public Optional<Genre> findGenreById(Long id) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        return jdbcTemplate.query(sqlQuery, mapper, id).stream().findFirst();
    }

    @Override
    public List<Genre> findGenresByFilmId(Long filmId) {
        String sql = "SELECT g.genre_id, g.genre_name FROM genres g JOIN film_genres fg" +
                " ON g.genre_id = fg.genre_id WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        }, filmId);
    }

    @Override
    public boolean containsGenre(Set<Genre> genres) {
        for (Genre genre : genres) {
            String query = "SELECT 1 FROM genres WHERE genre_id = ? LIMIT 1";
            try {
                jdbcTemplate.queryForObject(query, Boolean.class, genre.getId());
                return true;
            } catch (EmptyResultDataAccessException e) {
                return false;
            }
        }
        return false;
    }
}
