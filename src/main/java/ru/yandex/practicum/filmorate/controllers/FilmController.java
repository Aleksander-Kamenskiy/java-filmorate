package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;


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
        log.info("Создание фильма");
        Film newFilm = filmService.create(film);
        log.info("Фильм создан " + newFilm.getId());
        return newFilm;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        log.info("Обновление фильма");
        Film updateFilm = filmService.update(film);
        log.info("Фильм обновлен " + updateFilm.getId());
        return updateFilm;
    }

    @GetMapping(value = "/films/{id}")
    public Film getFilms(@PathVariable Integer id) {
        log.info("Получение фильма");
        Film getFilm = filmService.getFilm(id);
        log.info("Фильм получен " + getFilm.getId());
        return getFilm;
    }

    @GetMapping(value = "/films")
    public Collection<Film> getAll() {
        log.info("Получение фильмов");
        Collection<Film> getAllFilms = filmService.getAll();
        log.info("Все фильмы получены " + getAllFilms);
        return getAllFilms;
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получение лайка");
        filmService.addLike(id, userId);
        log.info("Лайк получен");
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Удаление лайка");
        filmService.deleteLike(id, userId);
        log.info("Лайк удален");
    }

    @GetMapping(value = "/films/popular")
    public Collection<Film> bestFilms(@RequestParam(name = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Получение популярных фильмов");
        List<Film> bestsFilms = filmService.bestFilmByLike(count);
        log.info("Все фильмы получены " + bestsFilms.size());
        return bestsFilms;
    }
}
