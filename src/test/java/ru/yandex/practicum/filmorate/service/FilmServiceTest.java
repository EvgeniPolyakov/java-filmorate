package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmServiceTest {
    private final FilmService filmService;

    Film createTestFilmEntity() {
        Film film = new Film();
        long timestamp = Instant.now().getEpochSecond();
        film.setName(String.format("testName %s", timestamp));
        film.setDescription(String.format("testDescription %s", timestamp));
        film.setReleaseDate(Date.valueOf("2000-01-01"));
        film.setDuration(100);
        film.setMpa(new MPARating(1L, "G"));
        film.setGenres(List.of(new Genre(1L, "Комедия")));
        return film;
    }

    @Test
    @Transactional
    void getFilmsTest() {
        assertThat(filmService.getFilms().isEmpty());
    }

    @Test
    @Transactional
    void getFilmByIdTest() {
        assertThrows(NotFoundException.class, () -> filmService.getFilmById(666L));
    }

    @Test
    @Transactional
    void createFilmWithWrongReleaseDateTest() {
        Film film = createTestFilmEntity();
        film.setReleaseDate(Date.valueOf("1888-01-01"));
        assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

    @Test
    @Transactional
    void updateFilmWithWrongReleaseDateTest() {
        Film film = filmService.createFilm(createTestFilmEntity());
        film.setReleaseDate(Date.valueOf("1888-01-01"));
        assertThrows(ValidationException.class, () -> filmService.updateFilm(film));
    }

    @Test
    @Transactional
    void addLikeTest() {
        assertThrows(NotFoundException.class, () -> filmService.addLike(666L, 666L));
    }

    @Test
    @Transactional
    void removeLikeTest() {
        assertThrows(NotFoundException.class, () -> filmService.removeLike(666L, 666L));
    }
}