package ru.yandex.practicum.filmorate.service;

public class IdGenerator {
    private static int filmBaseId;
    private static int userBaseId;

    private IdGenerator () {
        throw new AssertionError();
    }

    public static int generateFilmId() {
        return ++filmBaseId;
    }

    public static int generateUserId() {
        return ++userBaseId;
    }

    public static void setFilmBaseId(int filmBaseId) {
        IdGenerator.filmBaseId = filmBaseId;
    }

    public static void setUserBaseId(int userBaseId) {
        IdGenerator.userBaseId = userBaseId;
    }
}
