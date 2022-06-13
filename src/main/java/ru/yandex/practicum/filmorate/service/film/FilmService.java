package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        if (filmStorage.getFilms().get(id) == null) {
            throw new ObjectNotFoundException("Фильм не найден.");
        }
        return filmStorage.getFilms().get(id);
    }

    public Film createFilm(Film film) {
        filmStorage.create(film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmStorage.update(film);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        getFilmById(filmId).getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        getFilmById(filmId).getLikes().remove(userId);
    }

    public List<Film> getHighlyRatedFilms(Long count) {
        return filmStorage.getAllFilms()
                .stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return f1.getLikes().size() - f0.getLikes().size();
    }
}
