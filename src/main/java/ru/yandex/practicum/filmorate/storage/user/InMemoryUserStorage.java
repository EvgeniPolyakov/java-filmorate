package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.idgenerator.UserIdGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User create(User user) {
        validate(user);
        generateId(user);
        fillNameIfBlank(user);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (user == null) {
            throw new ObjectNotFoundException("Неверно переданы данные пользователя.");
        }
        if (!users.containsKey(user.getId())) {
            throw new ObjectNotFoundException("Пользователь не найден.");
        }
        validate(user);
        fillNameIfBlank(user);
        user.setFriends(users.get(user.getId()).getFriends());
        users.put(user.getId(), user);
        return user;
    }

    private void generateId(User user) {
        Long newId = UserIdGenerator.generateUserId();
        user.setId(newId);
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Имя пользователя не может содержать пробеды.");
        }
    }

    private void fillNameIfBlank(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    public Map<Long, User> getUsers() {
        return users;
    }

}
