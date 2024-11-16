package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма должно быть указано.")
    private String name;
    @NotBlank(message = "Описание фильма должно быть указано.")
    @Size(max = 200, message = "Описание фильма не может быть длиннее 200 символов.")
    private String description;
    @PastOrPresent(message = "Указана неверная дата релиза.")
    private LocalDate releaseDate;
    @NotNull(message = "Продолжительность фильма должна быть указана.")
    @Positive(message = "Продолжительность фильма должна являться положительным числом.")
    private Integer duration;
    private LinkedHashSet<Genre> genres;
    private Mpa mpa;

    public Film(String name, String description, LocalDate releaseDate, Integer duration, Integer mpaId) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = new Mpa(mpaId);
        this.genres = new LinkedHashSet<>();
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration, Integer mpaId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = new Mpa(mpaId);
        this.genres = new LinkedHashSet<>();
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = new Mpa();
        this.genres = new LinkedHashSet<>();
    }
}
