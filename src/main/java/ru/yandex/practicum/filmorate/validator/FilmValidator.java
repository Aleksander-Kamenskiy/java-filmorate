package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;


@Slf4j
public class FilmValidator {

    public void validate(Film film) {
        LocalDate date = LocalDate.of(1895, 12, 28);

        if (film.getReleaseDate().isBefore(date)) {
            log.error("неправильная дата фильма " + film.getId());
            throw new ValidationException("Ошибка" + film.getReleaseDate());
        }
        if (film.getName().isEmpty()) {
            log.error("неправильное имя фильма " + film.getId());
            throw new ValidationException(" Пустое имя фильма ");
        }
        if (film.getDescription().length() > 200) {
            log.error("неправильное описание фильма " + film.getId());
            throw new ValidationException(" больше 200 ");
        }
        if (film.getDuration() < 0) {
            log.error("отрицательное время фильма " + film.getId());
            throw new ValidationException(" минус ");
        }
        if (film.getId() == 0) {
            log.error(" id фильма 0 " + film.getId());
            throw new ValidationException(" id 0");
        }
    }
}
