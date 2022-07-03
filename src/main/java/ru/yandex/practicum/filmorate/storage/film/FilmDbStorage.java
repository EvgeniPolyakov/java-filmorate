package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Override
    public Collection<Film> getAllFilms() {
        return null;
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }
}
