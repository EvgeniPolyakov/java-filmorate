package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MPARating {
    private final Long id;
    private final String name;

    public MPARating(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
