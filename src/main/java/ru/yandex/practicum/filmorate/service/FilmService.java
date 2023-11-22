package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;


import java.util.*;


@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;

    FilmValidator validator = new FilmValidator();

    public List<Film> findAll() {
        List<Film> filmList = filmStorage.findAll();
        filmList.forEach(film -> film.setGenres(genreStorage.findGenresByFilmId(film.getId())));
        return filmList;
    }

    public Film findById(Integer id) {
        Film film = filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Film does not exist"));
        film.setGenres(genreStorage.findGenresByFilmId(film.getId()));
        return film;
    }

    public Film create(Film film) {
        validator.validate(film);
        Film createdFilm = filmStorage.create(film).orElseThrow(() -> new NotFoundException("Film not created"));
        if (film.getGenres() != null) {
            genreStorage.filmGenreBatchUpdate(createdFilm.getId(), film.getGenres());
        }
        createdFilm.setGenres(genreStorage.findGenresByFilmId(createdFilm.getId()));
        return createdFilm;
    }

    public Film update(Film film) {
        Integer id = film.getId();
        findById(id);
        validator.validateUpdate(film);
        Film updatedFilm = filmStorage.update(film).orElseThrow(() -> new NotFoundException("Film not updated"));
        genreStorage.deleteGenreByFilmId(id);
        if (film.getGenres() != null) {
            genreStorage.filmGenreBatchUpdate(id, film.getGenres());
        }
        updatedFilm.setGenres(genreStorage.findGenresByFilmId(id));
        return updatedFilm;
    }

    public Like addLike(Integer filmId, Integer userId) {
        Optional<Like> filmLike = likeStorage.findFilmLikeByUserIdFilmId(userId, filmId);
        return filmLike.orElseGet(() -> likeStorage.createFilmLike(userId, filmId).orElseThrow(() -> new NotFoundException("Like not added")));
    }

    public Like deleteLike(Integer filmId, Integer userId) {
        return likeStorage.deleteFilmLike(userId, filmId).orElseThrow(() -> new NotFoundException("Like does not exist"));
    }

    public List<Film> getPopular(int count) {
        List<Film> filmList = filmStorage.getPopular(count);
        filmList.forEach(film -> film.setGenres(genreStorage.findGenresByFilmId(film.getId())));
        return filmList;
    }
}
