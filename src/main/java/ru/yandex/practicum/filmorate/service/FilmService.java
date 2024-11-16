package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private Long idCounter = 0L;

    public Film getFilm(Long id) {
        filmContainCheck(id);
        return filmStorage.getFilm(id);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        filmReleaseDateCheck(film);
        if (!mpaStorage.containsMpa(film.getMpa().getId())) {
            throw new ValidationException("Не существует МПА рейтинг с ID: " + film.getMpa().getId());
        }
        if (film.getGenres() != null) {
            genreValidCheck(film);
        }
        film.setId(nextId());
        log.debug("Фильму присвоен ID{}.", film.getId());
        filmStorage.putFilm(film);
        log.info("Создан новый фильм.");
        return film;
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            log.error("Не указан ID фильма.");
            throw new ValidationException("Не указан ID фильма");
        }
        filmContainCheck(film.getId());
        if (!mpaStorage.containsMpa(film.getMpa().getId())) {
            throw new ValidationException("Не существует МПА рейтинг с ID: " + film.getMpa().getId());
        }        if (film.getGenres() != null) {
            genreValidCheck(film);
        }
        filmReleaseDateCheck(film);
        filmStorage.updateFilm(film);
        return film;
    }

    public void addLike(Long userId, Long filmId) {
        filmContainCheck(filmId);
        userContainCheck(userId);
        likeStorage.addLike(userId, filmId);
        log.info("Лайк от пользователя {} добавлен", userId);
    }

    public void removeLike(Long userId, Long filmId) {
        filmContainCheck(filmId);
        userContainCheck(userId);
        likeStorage.removeLike(userId, filmId);
        log.info("Лайк от пользователя {} убран", userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getMostPopular(count);
    }

    private void filmReleaseDateCheck(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Указана неверная дата релиза.");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1985 года.");
        }
    }

    private void filmContainCheck(Long id) {
        if (!filmStorage.containsFilmId(id)) {
            log.error("Не найден фильм с ID{}", id);
            throw new NotFoundException("Фильм с ID " + id + " не найден.");
        }
    }

    private void genreValidCheck(Film film) {
        if (!genreStorage.containsGenre(film.getGenres())) {
            throw new ValidationException("Не существует такого жанра");
        }
    }

    private void userContainCheck(Long userId) {
        if (!userStorage.containsUserId(userId)) {
            log.error("Не найден пользователь с ID{}", userId);
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }
    }

    private Long nextId() {
        return ++this.idCounter;
    }
}
