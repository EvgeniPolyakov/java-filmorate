package ru.yandex.practicum.filmorate.service.idgenerator;

public class FilmIdGenerator {
    private static Long filmBaseId = 0L;

    private FilmIdGenerator() {
        throw new AssertionError();
    }

    public static Long generateFilmId() {
        return ++filmBaseId;
    }

    public static void setFilmBaseId(Long filmBaseId) {
        FilmIdGenerator.filmBaseId = filmBaseId;
    }
}
