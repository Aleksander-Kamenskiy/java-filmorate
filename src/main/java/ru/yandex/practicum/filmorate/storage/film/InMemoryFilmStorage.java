package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final LinkedHashMap<Integer, Film> films = new LinkedHashMap<>();
    private int nextId = 1;
    FilmValidator validator = new FilmValidator();


    @Override
    public Film addFilm(Film film) {
        film.setId(nextId);
        validator.validate(film);
        nextId += 1;
        films.put(film.getId(), film);
        log.info("создан фильм " + film.getId());
        return film;
    }

    @Override
    public void deleteFilm(Integer idFilm) {
        films.remove(idFilm);
    }

    @Override
    public Film changeFilm(Film film) {
        validator.validateUpdate(film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Ошибка");
        }
        films.put(film.getId(), film);
        log.info("фильм обновлен " + film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return films.values().stream().collect(Collectors.toList());
    }

    @Override
    public Film findFilmById(int idFilm) {
        if (films.get(idFilm) == null) {
            throw new NotFoundException(String.format("Нет фильма с id %s", idFilm));
        }
        return films.get(idFilm);
    }
}
