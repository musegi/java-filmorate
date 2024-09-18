package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public void putFilm(Long id, Film film) {
        films.put(id, film);
    }

    @Override
    public Set<Long> getFilmsId() {
        return films.keySet();
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Long id) {
        return films.get(id);
    }

    @Override
    public boolean containsFilmId(Long id) {
        return films.containsKey(id);
    }
}
