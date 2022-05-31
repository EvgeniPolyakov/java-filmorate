package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
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
}
