package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTests {
    UserController userController;
    User user;

    @BeforeEach
    public void init() {
        userController = new UserController();
        user = new User(null, "test.email@gmail.com", "gospodi", null,
                LocalDate.of(1980, 6, 13));
    }

    @Test
    public void testCreateNullEmailUser() {
        user.setEmail(null);
        ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.",
                thrown.getMessage());
    }

    @Test
    public void testCreateLoginWithSpaceUser() {
        user.setLogin("help me");
        ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals("Логин не может быть пустым или содержать пробелы.", thrown.getMessage());
    }

    @Test
    public void testUpdateFutureBirthdayUser() {
        userController.create(user);
        user.setBirthday(LocalDate.of(2150, 10, 13));
        user.setId(1L);
        ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем.", thrown.getMessage());
    }

    @Test
    public void testUpdateNullIdUser() {
        userController.create(user);
        user.setId(null);
        ValidationException thrown = assertThrows(ValidationException.class, () -> userController.update(user));
        Assertions.assertEquals("Не указан ID пользователя", thrown.getMessage());
    }
}