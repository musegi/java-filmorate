package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre findById(long genreId) {
        Optional<Genre> genre = genreStorage.findGenreById(genreId);
        return genre.orElseThrow(() -> new NotFoundException("Не найден жанр с id=" + genreId));
    }

    public List<Genre> findByFilm(long filmId) {
        return genreStorage.findGenresByFilmId(filmId);
    }

    private void genreValidCheck(Film film) {
        if (!genreStorage.containsGenre(film.getGenres())) {
            throw new ValidationException("Не существует такого жанра");
        }
    }
}