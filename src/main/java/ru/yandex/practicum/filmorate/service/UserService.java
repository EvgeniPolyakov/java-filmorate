package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        validateNameAndLogin(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        validateUser(user.getId());
        return userStorage.update(user);
    }

    public User getUserById(Long id) {
        validateUser(id);
        return userStorage.getUserById(id);
    }

    public void deleteUser(Long id) {
        validateUser(id);
        userStorage.deleteUser(id);
    }

    public void addFriend(Long userId, Long friendId) {
        validateUser(userId);
        validateUser(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        validateUser(userId);
        validateUser(friendId);
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getUserFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        Set<User> userFriends = new HashSet<>(getUserFriends(userId));
        Set<User> otherUserFriends = new HashSet<>(getUserFriends(otherUserId));
        userFriends.retainAll(otherUserFriends);
        return new ArrayList<>(userFriends);
    }

    private void validateUser(Long id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException(String.format("Пользователь c id %s не найден.", id));
        }
        validateNameAndLogin(userStorage.getUserById(id));
    }

    private void validateNameAndLogin(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка: поле login не должно содержать пробелы");
        }
    }
}
