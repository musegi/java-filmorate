package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTests {
    UserService userService = new UserService(new InMemoryUserStorage());
    UserController userController;
    User user;

    @BeforeEach
    public void init() {
        userController = new UserController(userService);
        user = new User(null, "test.email@gmail.com", "gospodi", null,
                LocalDate.of(1980, 6, 13), Collections.emptySet());
    }

    @Test
    public void testCreateLoginWithSpaceUser() {
        user.setLogin("help me");
        ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals("Логин не может содержать пробелы.", thrown.getMessage());
    }

    @Test
    public void testUpdateNonExistentId() {
        userController.create(user);
        user.setId(8L);
        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                userController.update(user));

        Assertions.assertEquals("Пользователь с ID 8 не найден.", thrown.getMessage());
    }
}