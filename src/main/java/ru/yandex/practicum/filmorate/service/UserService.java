package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final InMemoryUserStorage userStorage;

    public User getUser(Long id) {
        userContainCheck(id);
        return userStorage.getUser(id);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        userLoginPatternCheck(user);
        User userChecked = userNameCheck(user);
        userChecked.setId(getNextId());
        log.debug("Пользователю присвоен ID{}.", userChecked.getId());
        userStorage.putUser(userChecked.getId(), userChecked);
        log.info("Создан новый пользователь.");
        return userChecked;
    }

    public User updateUser (User newUser) {
        userContainCheck(newUser.getId());
        User oldUser = userStorage.getUser(newUser.getId());
        userLoginPatternCheck(newUser);
        User newUserChecked = userNameCheck(newUser);
        if (!newUserChecked.getEmail().equals(oldUser.getEmail())) {
            log.info("Новый имейл присвоен.");
            oldUser.setEmail(newUserChecked.getEmail());
        }
        if (!newUserChecked.getLogin().equals(oldUser.getLogin())) {
            log.info("Новый логин присвоен.");
            oldUser.setLogin(newUserChecked.getLogin());
        }
        if (!newUserChecked.getName().equals(oldUser.getName())) {
            log.info("Новое имя присвоено.");
            oldUser.setName(newUserChecked.getName());
        }
        if (!newUserChecked.getBirthday().equals(oldUser.getBirthday())) {
            log.info("Новая дата рождения присвоена.");
            oldUser.setBirthday(newUserChecked.getBirthday());
        }
        return oldUser;
    }

    public User addFriend(Long id, Long friendId) {
        userContainCheck(id);
        userContainCheck(friendId);
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        userFriend.addFriend(id);
        user.addFriend(friendId);
        log.info("Пользователь {} и {} теперь друзья.", user.getId(), userFriend.getId());
        return user;
    }

    public User removeFriend(Long id, Long friendId) {
        userContainCheck(id);
        userContainCheck(friendId);
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        if (!user.getFriends().contains(friendId)) {
            log.error("Пользователи не друзья.");
            throw new NotFoundException("Пользователи не являются друзьями.");
        }
        userFriend.removeFriend(id);
        user.removeFriend(friendId);
        log.info("Пользователь {} и {} больше не друзья.", user.getId(), userFriend.getId());
        return user;
    }

    public List<User> allFriends(Long id) {
        userContainCheck(id);
        User user = userStorage.getUser(id);
        Set<Long> userFriends = user.getFriends();
        log.info("Список друзей {}", userFriends);

        return userFriends.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> allMutualFriends(Long id, Long otherId) {
        userContainCheck(id);
        userContainCheck(otherId);
        User user = userStorage.getUser(id);
        User otherUser = userStorage.getUser(otherId);
        return user.getFriends().stream()
                .filter(idUser -> otherUser.getFriends().contains(idUser))
                .map(userStorage::getUser)
                .collect(Collectors.toList());
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

    private long getNextId() {
        long currentMaxId = userStorage.getUsersId()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
