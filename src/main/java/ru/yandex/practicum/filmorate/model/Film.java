package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NotEmpty(message = "Название фильма должно быть указано.")
    @NotBlank(message = "Название фильма должно быть указано.")
    private String name;
    @NotEmpty(message = "Описание фильма должно быть указано.")
    @NotBlank(message = "Описание фильма должно быть указано.")
    @Size(max = 200, message = "Описание фильма не может быть длиннее 200 символов.")
    private String description;
    @PastOrPresent(message = "Указана неверная дата релиза.")
    private LocalDate releaseDate;
    @NotNull(message = "Продолжительность фильма должна быть указана.")
    @Positive(message = "Продолжительность фильма должна являться положительным числом.")
    private Integer duration;
    private Set<Long> likes = new HashSet<>();

    public void addLike(Long id) {
        likes.add(id);
    }

    public void removeLike(Long id) {
        likes.remove(id);
    }
}
