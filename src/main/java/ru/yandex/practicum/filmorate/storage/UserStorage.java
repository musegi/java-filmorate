package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    void putUser(Long id, User user);

    Set<Long> getUsersId();

    List<User> getUsers();

    User getUser(Long id);

    boolean containsUserId(Long id);

    Long nextId();
}
