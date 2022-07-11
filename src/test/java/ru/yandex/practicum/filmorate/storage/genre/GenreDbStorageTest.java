package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreStorage genreStorage;
    private final FilmStorage filmStorage;

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

    List<Genre> allGenresList() {
        List<Genre> allGenres = new ArrayList<>();
        allGenres.add(new Genre(1L, "Комедия"));
        allGenres.add(new Genre(2L, "Драма"));
        allGenres.add(new Genre(3L, "Мультфильм"));
        allGenres.add(new Genre(4L, "Триллер"));
        allGenres.add(new Genre(5L, "Документальный"));
        allGenres.add(new Genre(6L, "Боевик"));
        return allGenres;
    }

    @Test
    @Transactional
    void getGenreByIdTest() {
        assertThat(genreStorage.getGenreById(1L).getName()).isEqualTo("Комедия");
        assertThat(genreStorage.getGenreById(2L).getName()).isEqualTo("Драма");
        assertThat(genreStorage.getGenreById(3L).getName()).isEqualTo("Мультфильм");
        assertThat(genreStorage.getGenreById(4L).getName()).isEqualTo("Триллер");
        assertThat(genreStorage.getGenreById(5L).getName()).isEqualTo("Документальный");
        assertThat(genreStorage.getGenreById(6L).getName()).isEqualTo("Боевик");
    }

    @Test
    @Transactional
    void getGenresByFilmTest() {
        Film film = createTestFilmEntity();
        filmStorage.create(film);
        assertThat(genreStorage.getGenresByFilm(film.getId())).isEqualTo(film.getGenres());

    }

    @Test
    @Transactional
    void getAllGenresTest() {
        assertThat(genreStorage.getAllGenres()).isEqualTo(allGenresList());
    }

    @Test
    @Transactional
    void assignGenreToFilmTest() {
        Film film = createTestFilmEntity();
        filmStorage.create(film);
        List<Genre> newGenres = List.of(new Genre(2L, "Драма"), new Genre(6L, "Боевик"));
        genreStorage.assignGenreToFilm(film.getId(), newGenres);
        assertThat(genreStorage.getGenresByFilm(film.getId())).contains(newGenres.get(0), newGenres.get(1));
        assertThat(genreStorage.getGenresByFilm(film.getId())).hasSize(3);
    }

    @Test
    @Transactional
    void deleteGenresByGenreTest() {
        Film film = createTestFilmEntity();
        film.setGenres(List.of(new Genre(1L, "Комедия"), new Genre(6L, "Боевик")));
        genreStorage.deleteGenresByFilm(film.getId());
        assertThat(genreStorage.getGenresByFilm(film.getId()).isEmpty());
    }
}