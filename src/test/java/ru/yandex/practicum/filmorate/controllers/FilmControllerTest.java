package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void filmNameIsEmptyTest() throws ValidationException {
        Film film = new Film("", "Description", LocalDate.of(2022, 10, 10), 90);
        FilmController filmController = new FilmController();
        assertThrows(ValidationException.class, () -> filmController.create(film));
        Collection<Film> filmCollection = filmController.getAll();
        assertEquals(0, filmCollection.size());
    }

    @Test
    public void filmDescriptionLenght201Test() throws ValidationException {
        Film film = new Film("Name", "A".repeat(201), LocalDate.of(2022, 10, 10), 90);
        FilmController filmController = new FilmController();
        assertThrows(ValidationException.class, () -> filmController.create(film));
        Collection<Film> filmCollection = filmController.getAll();
        assertEquals(0, filmCollection.size());
    }

    @Test
    public void filmDescriptionLenght200Test() throws ValidationException {
        Film film = new Film("Name", "A".repeat(200), LocalDate.of(2022, 10, 10), 90);
        FilmController filmController = new FilmController();
        filmController.create(film);
        Collection<Film> filmCollection = filmController.getAll();
        Film testFilm = filmCollection.iterator().next();
        assertEquals(1, filmCollection.size());
        assertEquals("A".repeat(200), testFilm.getDescription());
    }

    @Test
    public void filmReleaseDateTest() throws ValidationException {
        Film film = new Film("Name", "Description", LocalDate.of(1895, 12, 27), 90);
        FilmController filmController = new FilmController();
        assertThrows(ValidationException.class, () -> filmController.create(film));
        Collection<Film> filmCollection = filmController.getAll();
        assertEquals(0, filmCollection.size());
    }

    @Test
    public void filmDurationTest() throws ValidationException {
        Film film = new Film("Name", "Description", LocalDate.of(1895, 12, 29), -1);
        FilmController filmController = new FilmController();
        assertThrows(ValidationException.class, () -> filmController.create(film));
        Collection<Film> filmCollection = filmController.getAll();
        assertEquals(0, filmCollection.size());
    }
}
