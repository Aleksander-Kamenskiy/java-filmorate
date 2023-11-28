package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;


import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;


@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void testFindFilmById() {
        Film film = Film.builder()
                .name("Titan")
                .description("description")
                .releaseDate(LocalDate.of(2022, 10, 10))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("G")
                        .build())
                .duration(90)
                .build();
        FilmStorage filmStorage = new FilmDbStorage(namedParameterJdbcTemplate);
        Film newFilm = filmStorage.createdFilm(film);
        int id = newFilm.getId();

        Film savedFilm = filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с указанным id не найден: " + id));

        assertEquals(film.getDescription(),savedFilm.getDescription());
        assertEquals(film.getName(),savedFilm.getName());
        assertEquals(film.getReleaseDate(),savedFilm.getReleaseDate());
        assertEquals(film.getDuration(),savedFilm.getDuration());
    }
}
