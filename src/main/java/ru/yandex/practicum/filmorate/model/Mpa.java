package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Mpa {
    private Long id;
    private String name;

    public Mpa(long ratingId) {
        this.id = ratingId;
    }
}