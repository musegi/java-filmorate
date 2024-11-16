package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User putUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    Optional<User> getUser(Long id);

    List<User> getFriends(Long id);

    void addFriend(Long id, Long friendId);

    void removeFriend(Long id, Long friendId);

    List<User> getCommonFriends(Long id, Long otherId);

    boolean containsUserId(Long userId);
}
