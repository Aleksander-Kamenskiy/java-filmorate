package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    @Test
    public void createFilmTest() throws ValidationException {
        Film film = new Film("Title", "Description", LocalDate.of(2022, 10, 10), 90);
        FilmController filmController = new FilmController();
        filmController.create(film);
        Collection<Film> filmCollection = filmController.getAll();
        assertEquals(1, filmCollection.size());
        Film testFilm = filmCollection.iterator().next();
        assertEquals("Title", testFilm.getName());
        assertEquals("Description", testFilm.getDescription());
        assertEquals(LocalDate.of(2022, 10, 10), testFilm.getReleaseDate());
        assertEquals(90, testFilm.getDuration());
        assertEquals(1, testFilm.getId());
    }
}
