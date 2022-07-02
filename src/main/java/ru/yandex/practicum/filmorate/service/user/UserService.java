package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        userStorage.create(user);
        return user;
    }

    public User updateUser(User user) {
        userStorage.update(user);
        return user;
    }

    public User getUserById(Long id) {
        validateUserId(id);
        return userStorage.getUsers().get(id);
    }

    public void addFriend(Long userId, Long friendId) {
        validateUserId(userId);
        validateUserId(friendId);
        getUserById(userId).getFriends().add(friendId);
//        getUserById(friendId).getFriends().add(userId); - убрал автодобавление друга в ответ без подтверждения
    }

    public void removeFriend(Long userId, Long friendId) {
        validateUserId(userId);
        validateUserId(friendId);
        getUserById(userId).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(userId);
    }

    public List<User> getUsersFriends(User user) {
        return getUserById(user.getId())
                .getFriends()
                .stream()
                .map(userStorage.getUsers()::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        Set<Long> userFriends = new HashSet<>(getUserById(userId).getFriends());
        Set<Long> otherUserFriends = new HashSet<>(getUserById(otherUserId).getFriends());
        userFriends.retainAll(otherUserFriends);
        return userFriends
                .stream()
                .map(userStorage.getUsers()::get)
                .collect(Collectors.toList());
    }

    public boolean checkIsFriendshipMutual(Long userId, Long friendId) {
        return getUserById(userId).getFriends().contains(friendId)
                && getUserById(friendId).getFriends().contains(userId);
    }

    private void validateUserId(Long id) {
        if (userStorage.getUsers().get(id) == null) {
            throw new NotFoundException(String.format("Пользователь c id %s не найден.", id));
        }
    }
}
