package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
public class Film {
    private Integer id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private Integer rate;

    private Mpa mpa;

    private List<Genre> genres;
}
