package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.idgenerator.FilmIdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final FilmIdGenerator idGenerator;

    @Autowired
    public InMemoryFilmStorage(FilmIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film create(Film film) {
        validate(film);
        generateId(film);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (film == null) {
            throw new NotFoundException("Неверно переданы данные фильма.");
        }
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден.");
        }
        validate(film);
        film.setLikes(films.get(film.getId()).getLikes());
        films.put(film.getId(), film);
        return film;
    }

    public Map<Long, Film> getFilms() {
        return films;
    }

    private void generateId(Film film) {
        Long newId = idGenerator.generateFilmId();
        film.setId(newId);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Указана некорректная дата выпуска фильма.");
        }
    }
}
