package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    FilmValidator validator = new FilmValidator();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        validator.validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validator.validateUpdate(film);
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilm(int idFilm) {
        return findFilmById(idFilm);
    }

    public void addLike(int idFilm, int idUser) {
        Film film = findFilmById(idFilm);
        User user = findUserById(idUser);
        film.getLikes().add(user.getId());
    }

    public void deleteLike(int idFilm, int idUser) {
        Film film = findFilmById(idFilm);
        User user = findUserById(idUser);
        film.getLikes().remove(user.getId());
    }

    public List<Film> bestFilmByLike(Integer count) {
        return filmStorage.bestByLike(count);
    }


    private Film findFilmById(int id) {
        Film film = filmStorage.findById(id);
        if (film == null) {
            throw new NotFoundException(String.format("Нет фильма с id %s", id));
        }
        return film;
    }

    private User findUserById(int id) {
        User user = userStorage.findById(id);
        if (user == null) {
            throw new NotFoundException(String.format("Нет пользователя с id %s", id));
        }
        return user;
    }
}
