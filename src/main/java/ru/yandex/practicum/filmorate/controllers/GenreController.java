package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreController {
    GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public List<Genre> findAllGenre() {
        log.info("Получение всех жанров");
        List<Genre> genres = genreService.findAllGenre();
        log.info("Все жанры получены");
        return genres;
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable int id) {
        log.info("Получение жанра");
        Genre genre = genreService.findGenreById(id);
        log.info("Жанр получен");
        return genre;
    }
}
