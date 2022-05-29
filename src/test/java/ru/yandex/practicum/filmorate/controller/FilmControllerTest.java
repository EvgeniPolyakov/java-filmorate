package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController controller = new FilmController();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @BeforeEach
    void beforeEach() {
        IdGenerator.setFilmBaseId(0);
        controller = new FilmController();
    }

    @Test
    void testFindAll() {
        Film film1 = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(160)
                .build();
        Film film2 = Film.builder()
                .name("name2")
                .description("description2")
                .releaseDate(LocalDate.of(1990, 11, 11))
                .duration(100)
                .build();

        controller.create(film1);
        controller.create(film2);

        List<Film> testArray = new ArrayList<>();
        testArray.add(film1);
        testArray.add(film2);

        assertEquals(testArray.toString(), controller.findAll().toString());
    }

    @Test
    void testFindAllWithNoData() {
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void testCreateFilm() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(160)
                .build();

        controller.create(film);

        assertNotNull(film, "Фильм не найден.");
        assertTrue(controller.findAll().contains(film));
    }

    @Test
    void testUpdateFilm() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(160)
                .build();

        controller.create(film);

        Film filmUpdate = Film.builder()
                .id(1)
                .name("updated name")
                .description("updated description")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(160)
                .build();

        controller.createOrUpdate(filmUpdate);
        assertNotNull(filmUpdate, "Фильм не найден.");
        assertFalse(controller.findAll().contains(film));
        assertTrue(controller.findAll().contains(filmUpdate));
    }

    @Test
    void testCreateFilmWithWrongReleaseDate() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(160)
                .build();

        assertThrows(ValidationException.class, () -> controller.create(film));
    }

    @Test
    void testCreateFilmWithReleaseDate1985December28() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(160)
                .build();

        controller.create(film);
        assertNotNull(film, "Фильм не найден.");
        assertTrue(controller.findAll().contains(film));
    }

    @Test
    void testCreateFilmWithEmptyName() {
        Film film = Film.builder()
                .name("")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(160)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testCreateFilmWithDescriptionLongerThan200Chars() {
        Film film = Film.builder()
                .name("name")
                .description("descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                        "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                        "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(160)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testCreateFilmWithEmptyDescription() {
        Film film = Film.builder()
                .name("name")
                .description("")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(160)
                .build();

        controller.create(film);
        assertTrue(controller.findAll().contains(film));
        assertTrue(film.getDescription().isEmpty());
    }

    @Test
    void testCreateFilmWithNegativeDuration() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(-1)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testCreateFilmWithDurationZero() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(0)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}