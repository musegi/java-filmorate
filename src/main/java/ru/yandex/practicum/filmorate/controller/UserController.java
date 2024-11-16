package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
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
    public void addFriends(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
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