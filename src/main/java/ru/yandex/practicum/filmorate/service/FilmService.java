package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public Film getFilm(Long id) {
        filmContainCheck(id);
        return filmStorage.getFilm(id);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        filmReleaseDateCheck(film);
        film.setId(getNextId());
        log.debug("Фильму присвоен ID{}.", film.getId());
        filmStorage.putFilm(film.getId(), film);
        log.info("Создан новый фильм.");
        return film;
    }

    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Не указан ID фильма.");
            throw new ValidationException("Не указан ID фильма");
        }
        filmContainCheck(newFilm.getId());
        Film oldFilm = filmStorage.getFilm(newFilm.getId());
        filmReleaseDateCheck(newFilm);
        if (!newFilm.getName().equals(oldFilm.getName())) {
            oldFilm.setName(newFilm.getName());
            log.info("Новое название фильма присвоено.");
        }
        if (!newFilm.getDescription().equals(oldFilm.getDescription())) {
            oldFilm.setDescription(newFilm.getDescription());
            log.info("Новое описание фильма присвоено.");
        }
        if (!newFilm.getDuration().equals(oldFilm.getDuration())) {
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Новая продолжительность фильма присвоена.");
        }
        if (!newFilm.getReleaseDate().equals(oldFilm.getReleaseDate())) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.info("Новая дата релиза фильма присвоена.");
        }
        return oldFilm;
    }

    public Film addLike(Long id, Long userId) {
        filmContainCheck(id);
        userContainCheck(userId);
        Film film = filmStorage.getFilm(id);
        film.addLike(userId);
        log.info("Лайк от пользователя {} добавлен", userId);
        return film;
    }

    public Film removeLike(Long id, Long userId) {
        filmContainCheck(id);
        userContainCheck(userId);
        Film film = filmStorage.getFilm(id);
        film.removeLike(userId);
        log.info("Лайк от пользователя {} убран", userId);
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
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

    private void userContainCheck(Long userId) {
        if (!userStorage.containsUserId(userId)) {
            log.error("Не найден пользователь с ID{}", userId);
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }
    }

    private long getNextId() {
        long currentMaxId = filmStorage.getFilmsId()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
