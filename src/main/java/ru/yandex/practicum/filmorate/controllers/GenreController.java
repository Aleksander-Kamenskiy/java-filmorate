package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService service;

    @GetMapping
    public ResponseEntity<List<Genre>> findAll() {
        List<Genre> genres = service.findAllGenre();
        return ResponseEntity.ok().body(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> findById(@PathVariable int id) {
        Genre genre = service.findGenreById(id);
        return ResponseEntity.ok().body(genre);
    }
}