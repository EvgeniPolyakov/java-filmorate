package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.idgenerator.UserIdGenerator;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserIdGenerator idGenerator = new UserIdGenerator();
    UserStorage userStorage = new InMemoryUserStorage(idGenerator);
    UserService userService = new UserService(userStorage);
    UserController controller = new UserController(userService);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @BeforeEach
    void beforeEach() {
        userStorage = new InMemoryUserStorage(idGenerator);
        userService = new UserService(userStorage);
        idGenerator.setUserBaseId(0L);
        controller = new UserController(userService);
    }

    @Test
    void testFindAll() {
        User user1 = User.builder()
                .name("name1")
                .email("email@email.com")
                .login("login")
                .birthday(LocalDate.of(1984, 11, 11))
                .build();
        User user2 = User.builder()
                .name("name2")
                .email("email2@email.com")
                .login("login2")
                .birthday(LocalDate.of(1981, 3, 3))
                .build();

        controller.createUser(user1);
        controller.createUser(user2);

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);

        assertEquals(expected.toString(), controller.getAllUsers().toString());
    }

    @Test
    void testFindAllWithNoData() {
        assertTrue(controller.getAllUsers().isEmpty());
    }

    @Test
    void testCreateUser() {
        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .login("login")
                .birthday(LocalDate.of(1984, 11, 11))
                .build();

        controller.createUser(user);

        assertNotNull(user, "Пользователь не найден.");
        assertTrue(controller.getAllUsers().contains(user));
    }

    @Test
    void testUpdateUser() {
        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .login("login")
                .birthday(LocalDate.of(1984, 11, 11))
                .build();

        controller.createUser(user);

        User userUpdate = User.builder()
                .id(1L)
                .email("updatedEmail@email.com")
                .login("updatedLogin")
                .birthday(LocalDate.of(1984, 12, 12))
                .name("updated name")
                .build();

        controller.updateUser(userUpdate);
        assertNotNull(userUpdate, "Пользователь не найден.");
        assertFalse(controller.getAllUsers().contains(user));
        assertTrue(controller.getAllUsers().contains(userUpdate));
    }

    @Test
    void testCreateUserWithLoginContainingWhitespaces() {
        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .login("login login")
                .birthday(LocalDate.of(1984, 11, 11))
                .build();

        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }

    @Test
    void testCreateUserWithEmptyLogin() {
        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .login("")
                .birthday(LocalDate.of(1984, 11, 11))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testCreateUserWithEmptyName() {
        User user = User.builder()
                .name("")
                .email("email@email.com")
                .login("login")
                .birthday(LocalDate.of(1984, 11, 11))
                .build();

        controller.createUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @ParameterizedTest
    @MethodSource("emailsForCreateUserWithWrongEmailsTest")
    void testCreateUserWithWrongEmails(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testCreateUserWithBirthdateFromFuture() {
        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .login("login")
                .birthday(LocalDate.of(2984, 11, 11))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    static Stream<Arguments> emailsForCreateUserWithWrongEmailsTest() {
        return Stream.of(
                Arguments.of(User.builder()
                        .name("name")
                        .email("email.com")
                        .login("login")
                        .birthday(LocalDate.of(1984, 11, 11))
                        .build()),
                Arguments.of(User.builder()
                        .name("name")
                        .email("@email.com")
                        .login("login")
                        .birthday(LocalDate.of(1984, 11, 11))
                        .build()),
                Arguments.of(User.builder()
                        .name("name")
                        .email("emailemailemailemailemailemailemailemailemailemailemailemailemailemailemail@email.com")
                        .login("login")
                        .birthday(LocalDate.of(1984, 11, 11))
                        .build()),
                Arguments.of(User.builder()
                        .name("name")
                        .email("электронная@")
                        .login("login")
                        .birthday(LocalDate.of(1984, 11, 11))
                        .build())
        );
    }
}