package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;
    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;
    @NotNull
    @Positive
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
