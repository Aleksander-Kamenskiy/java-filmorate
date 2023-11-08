package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final LinkedHashMap<Integer, Film> films = new LinkedHashMap<>();
    private int nextId = 1;


    @Override
    public Film add(Film film) {
        film.setId(nextId);
        nextId += 1;
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void delete(Integer idFilm) {
        films.remove(idFilm);
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Ошибка");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return films.values().stream().collect(Collectors.toList());
    }

    @Override
    public Film findById(int idFilm) {
        return films.get(idFilm);
    }

    @Override
    public List<Film> bestByLike(Integer count) {
        return getAll().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count).collect(Collectors.toList());
    }
}
