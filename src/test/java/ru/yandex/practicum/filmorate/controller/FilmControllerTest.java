package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.idgenerator.FilmIdGenerator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

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
    FilmIdGenerator idGenerator = new FilmIdGenerator();
    FilmStorage filmStorage = new InMemoryFilmStorage(idGenerator);
    FilmService filmService = new FilmService(filmStorage);
    FilmController controller = new FilmController(filmService);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @BeforeEach
    void beforeEach() {
        filmStorage = new InMemoryFilmStorage(idGenerator);
        filmService = new FilmService(filmStorage);
        idGenerator.setFilmBaseId(0L);
        controller = new FilmController(filmService);
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

        controller.createFilm(film1);
        controller.createFilm(film2);

        List<Film> expected = new ArrayList<>();
        expected.add(film1);
        expected.add(film2);

        assertEquals(expected.toString(), controller.getAllFilms().toString());
    }

    @Test
    void testFindAllWithNoData() {
        assertTrue(controller.getAllFilms().isEmpty());
    }

    @Test
    void testCreateFilm() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(160)
                .build();

        controller.createFilm(film);

        assertNotNull(film, "Фильм не найден.");
        assertTrue(controller.getAllFilms().contains(film));
    }

    @Test
    void testUpdateFilm() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(160)
                .build();

        controller.createFilm(film);

        Film filmUpdate = Film.builder()
                .id(1L)
                .name("updated name")
                .description("updated description")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(160)
                .build();

        controller.updateFilm(filmUpdate);
        assertNotNull(filmUpdate, "Фильм не найден.");
        assertFalse(controller.getAllFilms().contains(film));
        assertTrue(controller.getAllFilms().contains(filmUpdate));
    }

    @Test
    void testCreateFilmWithWrongReleaseDate() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(160)
                .build();

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void testCreateFilmWithReleaseDate1985December28() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(160)
                .build();

        controller.createFilm(film);
        assertNotNull(film, "Фильм не найден.");
        assertTrue(controller.getAllFilms().contains(film));
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

        controller.createFilm(film);
        assertTrue(controller.getAllFilms().contains(film));
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