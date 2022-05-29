package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен запрос GET /films");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (validateReleaseDate(film)) {
            throw new ValidationException("Указана некорректная дата выпуска фильма.");
        }
        int id = generateId(film);
        films.put(id, film);
        log.debug("Получен запрос POST. Добавлен фильм: {}. Текущее количество: {}", film, films.size());
        return film;
    }

    @PutMapping
    public Film createOrUpdate(@Valid @RequestBody Film film) throws ValidationException {
        if (validateReleaseDate(film)) {
            throw new ValidationException("Указана некорректная дата выпуска фильма.");
        }
        if (film.getId() < 1) {
            throw new ValidationException("Указан некорректный id.");
        }
        films.put(film.getId(), film);
        log.debug("Получен запрос PUT. Добавлен фильм: {}. Текущее количество фильмов: {}", film, films.size());
        return film;
    }

    private int generateId(Film film) {
        int newId = IdGenerator.generateFilmId();
        film.setId(newId);
        return newId;
    }

    private boolean validateReleaseDate(Film film) {
        return !film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 27));
    }
}
