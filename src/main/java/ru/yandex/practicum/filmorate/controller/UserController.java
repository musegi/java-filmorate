package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriends(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> allFriends(@PathVariable Long id) {
        return userService.allFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> allMutualFriends(@PathVariable Long id,
                                       @PathVariable Long otherId) {
        return userService.allMutualFriends(id, otherId);
    }
}