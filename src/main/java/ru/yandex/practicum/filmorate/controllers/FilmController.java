package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.LinkedHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {
    private final LinkedHashMap<Integer, Film> films = new LinkedHashMap<>();
    private int nextId = 1;
    FilmValidator validator = new FilmValidator();

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        film.setId(nextId);
        validator.validate(film);
        nextId += 1;
        films.put(film.getId(), film);
        log.info("создан фильм " + film.getId());
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        validator.validateUpdate(film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Ошибка");
        }
        films.put(film.getId(), film);
        log.info("фильм обновлен " + film.getId());
        return film;
    }

    @GetMapping("/films")
    public Collection<Film> getAll() {
        return films.values();
    }
}
