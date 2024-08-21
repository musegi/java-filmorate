package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            log.error("Электронная почта недействительна или не была указана.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || (user.getLogin().contains(" "))) {
            log.error("Логин содержит пробелы или не был указан.");
            throw new ValidationException("Логин не может быть пустым или содержать пробелы.");
        } else if (user.getBirthday().isAfter(LocalDate.ofInstant(Instant.now(), ZoneId.of("UTC")))) {
            log.error("Указана неверная дата рождения.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя не было указано");
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        log.debug("Пользователю присвоен ID{}.", user.getId());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь.");
        return user;
    }

    @PutMapping
    public User update (@Valid @RequestBody User newUserInfo) {
        if (newUserInfo.getId() == null) {
            log.error("Не указан ID пользователя.");
            throw new ValidationException("Не указан ID пользователя");
        } else if (users.containsKey(newUserInfo.getId())) {
            User oldUserInfo = users.get(newUserInfo.getId());
            if (newUserInfo.getEmail() != null) {
                if (!(newUserInfo.getEmail().contains("@"))) {
                    log.error("Электронная почта недействительна.");
                    throw new ValidationException("Электронная почта должна содержать символ @");}
                else {
                    log.info("Новый имейл присвоен.");
                    oldUserInfo.setEmail(newUserInfo.getEmail());
                }
            }
            if (newUserInfo.getName() != null) {
                log.info("Новое имя присвоено.");
                oldUserInfo.setName(newUserInfo.getName());
            }
            if (newUserInfo.getLogin() != null) {
                if (newUserInfo.getLogin().contains(" ")) {
                    log.error("Новый логин содержит пробелы.");
                    throw new ValidationException("Логин не может содержать пробелы.");
                }
                log.info("Новый логин присвоен.");
                oldUserInfo.setLogin(newUserInfo.getLogin());
            }
            if (newUserInfo.getBirthday() != null) {
                if (newUserInfo.getBirthday().isAfter(LocalDate.ofInstant(Instant.now(), ZoneId.of("UTC")))) {
                    log.error("Указана неверная новая дата рождения.");
                    throw new ValidationException("Дата рождения не может быть в будущем.");
                }
                log.info("Новая дата рождения присвоена.");
                oldUserInfo.setBirthday(newUserInfo.getBirthday());
            }
            return oldUserInfo;
        } else {
            log.error("Не найден пользовать с ID{}", newUserInfo.getId());
            throw new ValidationException("Пользователь с ID " + newUserInfo.getId() + " не найден.");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
