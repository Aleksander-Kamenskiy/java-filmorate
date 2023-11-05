package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;


@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping(value = "/films/{id}")
    public Film getFilms(@PathVariable Integer id) {
        return filmService.getFilm(id);
    }

    @GetMapping(value = "/films")
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public Collection<Film> bestFilms(@RequestParam(name = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.bestFilmByLike(count);
    }
}
