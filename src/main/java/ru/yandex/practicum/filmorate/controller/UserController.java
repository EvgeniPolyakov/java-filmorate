package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Получен запрос GET /users");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (checkLoginContainsWhitespaces(user)) {
            throw new ValidationException("Ошибка: login содержит пробелы.");
        }
        checkNameIsBlank(user);
        int id = generateId(user);
        users.put(id, user);
        log.debug("Получен запрос POST. Добавлен пользователь: {}. Текущее количество: {}", user, users.size());
        return user;
    }

    @PutMapping
    public User createOrUpdate(@Valid @RequestBody User user) throws ValidationException {
        if (checkLoginContainsWhitespaces(user)) {
            throw new ValidationException("Ошибка: login содержит пробелы.");
        }
        if (user.getId() < 1) {
            throw new ValidationException("Указан некорректный id.");
        }
        checkNameIsBlank(user);
        users.put(user.getId(), user);
        log.debug("Получен запрос PUT. Добавлен пользователь: {}. Текущее количество: {}", user, users.size());
        return user;
    }

    private int generateId(User user) {
        int newId = IdGenerator.generateUserId();
        user.setId(newId);
        return newId;
    }

    private boolean checkLoginContainsWhitespaces(User user) {
        return user.getLogin().contains(" ");
    }

    private void checkNameIsBlank(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}