package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.*;


import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findAll();

    Optional<Film> findById(Integer id);

    Film createdFilm(Film film);

    void update(Film film);

    void deleteFilmById(Integer id);

    List<Film> getPopular(int count);

    void createLike(User user, Film film);

    void deleteLike(User user, Film film);

    List<Genre> findAllGenre();

    Optional<Genre> findGenreById(Integer id);

    List<Genre> findGenresByFilmId(Integer filmId);

    void filmGenreBatchUpdate(Integer filmId, List<Genre> genreList);

    void deleteGenreByFilmId(Integer filmId);

    List<Mpa> findAllMpa();

    Optional<Mpa> findMpaById(Integer id);

}
