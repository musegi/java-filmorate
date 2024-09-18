package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTests {

    FilmController filmController;
    Film film;

    @BeforeEach
    public void init() {
        filmController = new FilmController();
        film = new Film(null, "Исходный код", "Солдат по имени Коултер мистическим образом " +
                "оказывается в теле неизвестного мужчины, погибшего в железнодорожной катастрофе.",
                LocalDate.of(2011, 3, 31), 93);
    }

    @Test
    public void testCreateNullNameFilm() {
        film.setName(null);
        ValidationException thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Assertions.assertEquals("Название фильма не может быть пустым.", thrown.getMessage());
    }

    @Test
    public void testCreateLongDescriptionFilm() {
        film.setDescription("Солдат по имени Коултер мистическим образом оказывается в теле неизвестного мужчины, " +
                "погибшего в железнодорожной катастрофе. Коултер вынужден переживать чужую смерть снова и снова" +
                " до тех пор, пока не поймет, кто – зачинщик катастрофы.");
        ValidationException thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Assertions.assertEquals("Описание фильма должно быть указано и не может быть длиннее 200 символов.",
                thrown.getMessage());
    }

    @Test
    public void testCreateNegativeDurationFilm() {
        film.setDuration(-54);
        ValidationException thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть указана и являться положительным числом.",
                thrown.getMessage());
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
        ValidationException thrown = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        Assertions.assertEquals("Фильм с ID " + film.getId() + " не найден.", thrown.getMessage());
    }
}
