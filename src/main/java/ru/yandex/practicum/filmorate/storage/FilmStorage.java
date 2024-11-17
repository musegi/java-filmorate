package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film putFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilm(Long id);

    boolean containsFilmId(Long userId);

    List<Film> getMostPopular(int limit);
}
