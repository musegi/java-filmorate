package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Genre {
    private Long id;
    private String name;
}