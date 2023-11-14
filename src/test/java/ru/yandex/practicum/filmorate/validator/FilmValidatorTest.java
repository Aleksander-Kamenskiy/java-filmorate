package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class FilmValidatorTest {

    private FilmValidator filmValidator;

    @BeforeEach
    void setUp() {
        filmValidator = new FilmValidator();
    }

    @Test
    void filmNameIsEmptyTest() throws ValidationException {
        Film film = new Film("", "Description", LocalDate.of(2022, 10, 10), 90);

        assertThrows(ValidationException.class, () -> filmValidator.validate(film));

    }

    @Test
    void filmDescriptionLenght201Test() throws ValidationException {
        Film film = new Film("Name", "A".repeat(201), LocalDate.of(2022, 10, 10), 90);

        assertThrows(ValidationException.class, () -> filmValidator.validate(film));
    }

    @Test
    public void filmDescriptionLenght200Test() throws ValidationException {
        Film film = new Film("Name", "A".repeat(200), LocalDate.of(2022, 10, 10), 90);

        filmValidator.validate(film);
    }

    @Test
    void filmReleaseDateTest() throws ValidationException {
        Film film = new Film("Name", "Description", LocalDate.of(1895, 12, 27), 90);

        assertThrows(ValidationException.class, () -> filmValidator.validate(film));
    }

    @Test
    void filmDurationTest() throws ValidationException {
        Film film = new Film("Name", "Description", LocalDate.of(1895, 12, 29), -1);

        assertThrows(ValidationException.class, () -> filmValidator.validate(film));
    }
}
