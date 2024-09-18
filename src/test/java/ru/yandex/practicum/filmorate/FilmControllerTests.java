package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTests {

    FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
    FilmController filmController;
    Film film;

    @BeforeEach
    public void init() {
        filmController = new FilmController(filmService);
        film = new Film(null, "Исходный код", "Солдат по имени Коултер мистическим образом " +
                "оказывается в теле неизвестного мужчины, погибшего в железнодорожной катастрофе.",
                LocalDate.of(2011, 3, 31), 93, Collections.emptySet());
    }

    @Test
    public void testUpdateIncorrectReleaseDateFilm() {
        filmController.createFilm(film);
        film.setReleaseDate(LocalDate.of(1037, 9, 23));
        film.setId(1L);
        ValidationException thrown = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        Assertions.assertEquals("Дата релиза не может быть раньше 28 декабря 1985 года.", thrown.getMessage());
    }

    @Test
    public void testUpdateIncorrectIdFilm() {
        filmController.createFilm(film);
        film.setId(85L);
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> filmController.updateFilm(film));
        Assertions.assertEquals("Фильм с ID " + film.getId() + " не найден.", thrown.getMessage());
    }
}
