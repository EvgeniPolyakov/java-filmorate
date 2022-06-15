package ru.yandex.practicum.filmorate.service.idgenerator;

import org.springframework.stereotype.Component;

@Component
public class FilmIdGenerator {
    private Long filmBaseId = 0L;

    public Long generateFilmId() {
        return ++filmBaseId;
    }

    public void setFilmBaseId(Long filmBaseId) {
        this.filmBaseId = filmBaseId;
    }
}
