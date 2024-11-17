package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = "Email должен быть указан.")
    @Email(message = "Укажите корректный Email.")
    private String email;
    @NotBlank(message = "Логин должен быть указан.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения должна быть указана и не может быть в будущем.")
    private LocalDate birthday;
}