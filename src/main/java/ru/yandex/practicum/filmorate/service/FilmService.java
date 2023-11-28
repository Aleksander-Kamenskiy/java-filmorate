package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;


import java.util.*;


@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    FilmValidator validator = new FilmValidator();

    public List<Film> findAll() {
        List<Film> filmList = filmStorage.findAll();

        return filmList;
    }

    public Film findById(Integer id) {
        Film film = filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм не существует"));
        return film;
    }

    public User findUserById(Integer id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public Film create(Film film) {
        checkGenre(film);
        validator.validate(film);
        Film createdFilm = filmStorage.createdFilm(film);
        return createdFilm;
    }

    private void checkGenre(Film film) {
        if (film.getGenres() == null) {
            return;
        }
        HashSet<Integer> genreIds = new HashSet<>();
        for (Genre genre : filmStorage.findAllGenre()) {
            genreIds.add(genre.getId());
        }
        for (Genre genre : film.getGenres()) {
            if (!genreIds.contains(genre.getId())) {
                throw new NotFoundException("Жанр не найден");
            }
        }
    }

    public Film update(Film film) {
        validator.validateUpdate(film);
        findById(film.getId());
        filmStorage.update(film);
        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = findById(filmId);
        User user = findUserById(userId);
        filmStorage.createLike(user, film);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Film film = findById(filmId);
        User user = findUserById(userId);
        filmStorage.deleteLike(user, film);
    }

    public List<Film> getPopular(int count) {
        List<Film> filmList = filmStorage.getPopular(count);
        return filmList;
    }

    public List<Genre> findAllGenre() {
        return filmStorage.findAllGenre();
    }

    public Genre findGenreById(Integer id) {
        return filmStorage.findGenreById(id).orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }

    public List<Mpa> getAllMpa() {
        return filmStorage.findAllMpa();
    }

    public Mpa findMpaById(int id) {
        Optional<Mpa> mpa = filmStorage.findMpaById(id);
        return mpa.orElseThrow(() -> new NotFoundException("Рейтинг MPA с id " + id + " не найден"));
    }
}
