package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    @Override
    public User putUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        Long id = BaseDbStorage.insert(jdbc, sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbc.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<User> getUser(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id=?";
        return jdbc.query(sqlQuery, mapper, id).stream().findFirst();
    }

    @Override
    public List<User> getFriends(Long id) {
        String sqlQuery =
                "SELECT * FROM users WHERE user_id IN (SELECT user2_id FROM friends WHERE user1_id = ?)";
        return jdbc.query(sqlQuery, mapper, id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO friends (user1_id, user2_id) VALUES (?, ?)";
        jdbc.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user1_id = ? AND user2_id = ?";
        jdbc.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        String sqlQuery = "SELECT u2.* FROM friends f1 " +
                "INNER JOIN friends f2 ON f1.user2_id = f2.user2_id " +
                "INNER JOIN users u2 ON f2.user2_id = u2.user_id " +
                "WHERE f1.user1_id = ? AND f2.user1_id = ?";
        return jdbc.query(sqlQuery, mapper, firstUserId, secondUserId);
    }

    public boolean containsUserId(Long userId) {
        String sqlQuery = "SELECT 1 FROM users WHERE user_id = ? LIMIT 1";
        try {
            jdbc.queryForObject(sqlQuery, Boolean.class, userId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
