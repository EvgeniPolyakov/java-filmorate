package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Long id;
    @NotBlank
    @NotEmpty
    @Email
    private String email;
    @NotBlank
    @NotEmpty
    private String login;
    @NotNull
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    @JsonIgnore
    private Set<Long> friends;
}
