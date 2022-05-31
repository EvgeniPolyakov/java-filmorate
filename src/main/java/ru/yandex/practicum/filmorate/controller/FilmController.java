package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
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
    public Film create(@Valid @RequestBody Film film)  {
        generateId(film);
        validate(film);
        films.put(film.getId(), film);
        log.debug("Получен запрос POST. Добавлен фильм: {}. Текущее количество: {}", film, films.size());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        films.put(film.getId(), film);
        log.debug("Получен запрос PUT. Добавлен фильм: {}. Текущее количество фильмов: {}", film, films.size());
        return film;
    }

    private void generateId(Film film) {
        int newId = IdGenerator.generateFilmId();
        film.setId(newId);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Указана некорректная дата выпуска фильма.");
        }
        if (film.getId() < 1) {
            throw new ValidationException("ID фильма должен быть больше 0.");
        }
    }
}
