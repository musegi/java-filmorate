package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper mapper;
    private final GenreRowMapper genreMapper;

    @Override
    public Film putFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        Long id = BaseDbStorage.insert(jdbcTemplate, sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        if (film.getGenres() != null) {
        String sqlQuerySetGenres = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            film.getGenres().forEach(genre -> jdbcTemplate.update(sqlQuerySetGenres, id, genre.getId()));
        }
        return film;

    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            String sqlQuerySetGenres = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            film.getGenres().forEach(genre -> jdbcTemplate.update(sqlQuerySetGenres, film.getId(), genre.getId()));
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT f.*, mpa.mpa_name " +
                "FROM FILMS as f " +
                "LEFT JOIN mpa on f.MPA_ID = mpa.MPA_ID";
        List<Film> films = jdbcTemplate.query(sqlQuery, mapper);
        return addGenresToFilmList(films);
    }

    @Override
    public Film getFilm(Long id) {
        String sqlQuery = "SELECT f.*, mpa.mpa_name " +
                "FROM FILMS as f " +
                "LEFT JOIN mpa on f.MPA_ID = mpa.MPA_ID " +
                "WHERE f.FILM_ID = ?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, mapper, id);
        return addGenresToSingleFilm(film);
    }

    @Override
    public boolean containsFilmId(Long filmId) {
        String sqlQuery = "SELECT 1 FROM films WHERE film_id = ? LIMIT 1";
        try {
            jdbcTemplate.queryForObject(sqlQuery, Boolean.class, filmId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public List<Film> getMostPopular(int limit) {
        String sqlQuery = "SELECT f.*, mpa.mpa_name FROM films AS f " +
                "LEFT JOIN likes ON f.film_id = likes.film_id " +
                "LEFT JOIN mpa on f.MPA_ID = mpa.MPA_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT ?;";
        List<Film> films = jdbcTemplate.query(sqlQuery, mapper, limit);
        return addGenresToFilmList(films);
    }

    private Film addGenresToSingleFilm(Film film) {
        String sqlGenres = "SELECT g.genre_id, g.genre_name " +
                "FROM genres g " +
                "JOIN film_genres fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlGenres, genreMapper, film.getId());
        film.getGenres().addAll(genres);
        return film;
    }

    private List<Film> addGenresToFilmList(List<Film> films) {
        String getAllGenresQuery = "SELECT fg.film_id, g.genre_id, g.genre_name " +
                "FROM film_genres fg " +
                "JOIN genres g ON fg.genre_id = g.genre_id " +
                "GROUP BY fg.film_id, g.GENRE_ID " +
                "ORDER BY g.genre_id";
        Map<Long, List<Genre>> genresMap = new HashMap<>();
        jdbcTemplate.query(getAllGenresQuery, rs -> {
            long filmId = rs.getLong("film_id");
            genresMap.computeIfAbsent(filmId, l -> new ArrayList<>())
                    .add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
        });
        for (Film film : films) {
            long filmId = film.getId();
            if (genresMap.containsKey(filmId)) {
                List<Genre> genres = genresMap.get(filmId);
                genres.sort(Comparator.comparingLong(Genre::getId));
                film.getGenres().addAll(genres);
            }
        }
        return films;
    }
}
