package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    @NotEmpty(message = "Email должен быть указан.")
    @NotBlank(message = "Email должен быть указан.")
    @Email(message = "Укажите корректный Email.")
    private String email;
    @NotEmpty(message = "Логин должен быть указан.")
    @NotBlank(message = "Логин должен быть указан.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения должна быть указана и не может быть в будущем.")
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }

}