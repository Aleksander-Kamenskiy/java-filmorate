package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> findAllGenre() {
        return genreStorage.findAllGenre();
    }

    public Genre findGenreById(Integer id) {
        return genreStorage.findGenreById(id).orElseThrow(() -> new NotFoundException("Genre does not exist"));
    }

}
