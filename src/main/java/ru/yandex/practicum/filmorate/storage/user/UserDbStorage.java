package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public Map<Long, User> getUsers() {
        return null;
    }
}
