package ru.yandex.practicum.filmorate.service.idgenerator;

import org.springframework.stereotype.Component;

@Component
public class UserIdGenerator {
    private Long userBaseId = 0L;

    public Long generateUserId() {
        return ++userBaseId;
    }

    public void setUserBaseId(Long userBaseId) {
        this.userBaseId = userBaseId;
    }
}
