package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        return filmStorage.changeFilm(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(int idFilm) {
        return filmStorage.findFilmById(idFilm);
    }

    public void addLike(int idFilm, int idUser) {
        Film film = filmStorage.findFilmById(idFilm);
        User user = userStorage.findUserById(idUser);
        film.getLikes().add(user.getId());
    }

    public void deleteLike(int idFilm, int idUser) {
        Film film = filmStorage.findFilmById(idFilm);
        User user = userStorage.findUserById(idUser);
        film.getLikes().remove(user.getId());
    }

    public List<Film> bestFilmByLike(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count).collect(Collectors.toList());
    }

    public Film findFilmById(int id) {
        if (id <= 0) {
            throw new NotFoundException(String.format("Фильм с id %s не найден", id));
        }
        Film film = filmStorage.getAllFilms().get(id);
        return film;
    }
}
