package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    List<Genre> findAllGenre();

    Optional<Genre> findGenreById(Integer id);

    Set<Genre> findGenreByIds(Set<Genre> genreSet);

}
