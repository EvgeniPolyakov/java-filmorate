package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.idgenerator.FilmIdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

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
            throw new ObjectNotFoundException("Неверно переданы данные фильма.");
        }
        if (!films.containsKey(film.getId())) {
            throw new ObjectNotFoundException("Фильм не найден.");
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
        Long newId = FilmIdGenerator.generateFilmId();
        film.setId(newId);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Указана некорректная дата выпуска фильма.");
        }
    }
}
