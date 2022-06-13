package ru.yandex.practicum.filmorate.service.idgenerator;

public class UserIdGenerator {
    private static Long userBaseId = 0L;

    private UserIdGenerator () {
        throw new AssertionError();
    }

    public static Long generateUserId() {
        return ++userBaseId;
    }

    public static void setUserBaseId(Long userBaseId) {
        UserIdGenerator.userBaseId = userBaseId;
    }
}
