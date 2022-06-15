package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
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
    private Set<Long> friends;

}
