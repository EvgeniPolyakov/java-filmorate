package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    @NotEmpty
    private String name;
    @NotBlank
    @NotEmpty
    @Size(max = 200)
    private String description;
    private Date releaseDate;
    @Positive
    private int duration;
    private MPARating mpa;
    private List<Genre> genres;
}
