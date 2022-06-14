package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Collection<User> getAllUsers();

    User create(User user);

    User update(User user);

    Map<Long, User> getUsers();

}