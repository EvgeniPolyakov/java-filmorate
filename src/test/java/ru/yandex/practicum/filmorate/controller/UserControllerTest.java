package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

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
    UserController controller = new UserController();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @BeforeEach
    void beforeEach() {
        IdGenerator.setUserBaseId(0);
        controller = new UserController();
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

        controller.create(user1);
        controller.create(user2);

        List<User> testArray = new ArrayList<>();
        testArray.add(user1);
        testArray.add(user2);

        assertEquals(testArray.toString(), controller.findAll().toString());
    }

    @Test
    void testFindAllWithNoData() {
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void testCreateUser() {
        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .login("login")
                .birthday(LocalDate.of(1984, 11, 11))
                .build();

        controller.create(user);

        assertNotNull(user, "Пользователь не найден.");
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void testUpdateUser() {
        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .login("login")
                .birthday(LocalDate.of(1984, 11, 11))
                .build();

        controller.create(user);

        User userUpdate = User.builder()
                .id(1)
                .email("updatedEmail@email.com")
                .login("updatedLogin")
                .birthday(LocalDate.of(1984, 12, 12))
                .name("updated name")
                .build();

        controller.createOrUpdate(userUpdate);
        assertNotNull(userUpdate, "Пользователь не найден.");
        assertFalse(controller.findAll().contains(user));
        assertTrue(controller.findAll().contains(userUpdate));
    }

    @Test
    void testCreateUserWithLoginContainingWhitespaces() {
        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .login("login login")
                .birthday(LocalDate.of(1984, 11, 11))
                .build();

        assertThrows(ValidationException.class, () -> controller.create(user));
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

        controller.create(user);
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