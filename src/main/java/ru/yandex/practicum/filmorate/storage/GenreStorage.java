package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    List<Genre> findAll();

    Optional<Genre> findGenreById(Long id);

    List<Genre> findGenresByFilmId(Long filmId);

    boolean containsGenre(Set<Genre> genres);
}
