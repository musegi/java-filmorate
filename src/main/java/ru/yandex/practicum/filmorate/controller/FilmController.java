package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Не указано название фильма");
            throw new ValidationException("Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            log.error("Указанное описание фильма слишком длинное");
            throw new ValidationException("Описание фильма не может быть длиннее 200 символов.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Указана неверная дата релиза фильма.");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1985 года.");
        } else if (film.getDuration() <= 0) {
            log.error("Указана отрицательная продолжительность фильма.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }

        film.setId(getNextId());
        log.debug("Фильму присвоен ID{}.", film.getId());
        films.put(film.getId(), film);
        log.info("Создан новый фильм.");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilmInfo) {
        if (newFilmInfo.getId() == null) {
            log.error("Не указан ID фильма.");
            throw new ValidationException("Не указан ID фильма");
        }
        if (films.containsKey(newFilmInfo.getId())) {
            Film oldFilmInfo = films.get(newFilmInfo.getId());
            if (newFilmInfo.getDescription() != null) {
                if (newFilmInfo.getDescription().length() > 200) {
                    log.error("Указанное новое описание фильма слишком длинное");
                    throw new ValidationException("Описание фильма не может быть длиннее 200 символов");
                } else {
                    log.info("Новое описание присвоено.");
                    oldFilmInfo.setDescription(newFilmInfo.getDescription());
                }
            }
            if (newFilmInfo.getReleaseDate() != null) {
                if (newFilmInfo.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                    log.error("Указана неверная новая дата релиза фильма.");
                    throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1985 года.");
                } else {
                    log.info("Присвоена новая дата релиза.");
                    oldFilmInfo.setReleaseDate(newFilmInfo.getReleaseDate());
                }
            }
            if (newFilmInfo.getDuration() != null) {
                if (newFilmInfo.getDuration() <= 0) {
                    log.error("Указана отрицательная новая продолжительность фильма.");
                    throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
                } else {
                    log.info("Присвоена новая длительность фильма.");
                    oldFilmInfo.setDuration(newFilmInfo.getDuration());
                }
            }
            if (newFilmInfo.getName() != null) {
                log.info("Присвоено новое название фильма.");
                oldFilmInfo.setName(newFilmInfo.getName());
            }
            return oldFilmInfo;
        } else {
            log.error("Не найден фильм с ID{}", newFilmInfo.getId());
            throw new ValidationException("Фильм с ID " + newFilmInfo.getId() + " не найден.");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
