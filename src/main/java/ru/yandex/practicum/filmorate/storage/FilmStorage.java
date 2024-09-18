package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    void putFilm(Long id, Film film);

    Set<Long> getFilmsId();

    List<Film> getFilms();

    Film getFilm(Long id);

    boolean containsFilmId(Long id);
}
