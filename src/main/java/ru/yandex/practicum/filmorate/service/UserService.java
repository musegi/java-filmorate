package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public Optional<User> getUser(Long id) {
        userContainCheck(id);
        return userStorage.getUser(id);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        userLoginPatternCheck(user);
        User userChecked = userNameCheck(user);
        log.debug("Пользователю присвоен ID{}.", userChecked.getId());
        userStorage.putUser(userChecked);
        log.info("Создан новый пользователь.");
        return userChecked;
    }

    public User updateUser(User user) {
        userContainCheck(user.getId());
        userStorage.updateUser(user);
        log.info("Пользователь ID{} успешно обновлён!", user.getId());
        return user;
    }

    public void addFriend(Long id, Long friendId) {
        userContainCheck(id);
        userContainCheck(friendId);
        userStorage.addFriend(id, friendId);
        log.info("Пользователь {} добавил пользователя {} в друзья.", id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        userContainCheck(id);
        userContainCheck(friendId);
           userStorage.removeFriend(id, friendId);
            log.info("Пользователь {} больше не дружит с пользователем {}.", id, friendId);
    }

    public List<User> allFriends(Long id) {
        userContainCheck(id);
        return userStorage.getFriends(id);
    }

    public List<User> allMutualFriends(Long id, Long otherId) {
        userContainCheck(id);
        userContainCheck(otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    private void userContainCheck(Long userId) {
        if (userId == null) {
            log.error("Не указан ID пользователя.");
            throw new ValidationException("Не указан ID пользователя");
        }
        if (!userStorage.containsUserId(userId)) {
            log.error("Не найден пользователь с ID{}", userId);
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }
    }

    private void userLoginPatternCheck(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Логин содержит пробелы.");
            throw new ValidationException("Логин не может содержать пробелы.");
        }
    }

    private User userNameCheck(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя не было указано");
            user.setName(user.getLogin());
        }
        return user;
    }
}
