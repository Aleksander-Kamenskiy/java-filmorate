package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface FilmStorage {
    Film add(Film film);

    void delete(Integer idFilm);

    Film update(Film film);

    List<Film> getAll();

    Film findById(int idFilm);

    List<Film> bestByLike(Integer count);
}
