package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Film createFilm(FilmStorage filmStorage) {
        Film film = Film.builder()
                .name("Titan")
                .description("description")
                .releaseDate(LocalDate.of(2022, 10, 10))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("G")
                        .build())
                .duration(90)
                .build();
        return filmStorage.createdFilm(film);
    }

    private User createUser(UserStorage userStorage) {
        User user = User.builder()
                .name("Nikolai")
                .email("1mail@mail.ru")
                .login("bonbon1")
                .birthday(LocalDate.of(2000, 1, 16))
                .build();
        return userStorage.create(user);
    }


    @Test
    public void testFindFilmById() {
        FilmStorage filmStorage = new FilmDbStorage(namedParameterJdbcTemplate);
        Film newFilm = createFilm(filmStorage);
        int id = newFilm.getId();
        Film savedFilm = filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм с указанным id не найден: " + id));

        assertEquals(newFilm.getDescription(), savedFilm.getDescription());
        assertEquals(newFilm.getName(), savedFilm.getName());
        assertEquals(newFilm.getReleaseDate(), savedFilm.getReleaseDate());
        assertEquals(newFilm.getDuration(), savedFilm.getDuration());
    }

    @Test
    public void testUpdateFilm() {
        FilmStorage filmStorage = new FilmDbStorage(namedParameterJdbcTemplate);

        Film newFilm = createFilm(filmStorage);

        newFilm.setName("Titan and Princess");
        newFilm.setDescription("description2");
        newFilm.setReleaseDate(LocalDate.of(2000, 9, 9));
        newFilm.setMpa(Mpa.builder()
                .id(2)
                .name("PG")
                .build());
        newFilm.setDuration(101);
        filmStorage.update(newFilm);
        assertEquals("Titan and Princess", filmStorage.findAll().get(0).getName());
        assertEquals("description2", filmStorage.findAll().get(0).getDescription());
        assertEquals(LocalDate.of(2000, 9, 9), filmStorage.findAll().get(0).getReleaseDate());
        assertEquals((Mpa.builder()
                .id(2)
                .name("PG")
                .build()), filmStorage.findAll().get(0).getMpa());

        assertEquals(101, filmStorage.findAll().get(0).getDuration());
    }

    @Test
    public void testFindAllFilms() {
        FilmStorage filmStorage = new FilmDbStorage(namedParameterJdbcTemplate);
        Film newFilm = createFilm(filmStorage);
        Film newFilm2 = createFilm(filmStorage);

        assertThat(filmStorage.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testDeleteFilm() {
        FilmStorage filmStorage = new FilmDbStorage(namedParameterJdbcTemplate);
        Film newFilm = createFilm(filmStorage);
        filmStorage.deleteFilmById(newFilm.getId());

        assertThat(filmStorage.findAll().size()).isEqualTo(0);
    }

    @Test
    public void testGetPopularFilm() {
        FilmStorage filmStorage = new FilmDbStorage(namedParameterJdbcTemplate);
        Film newFilm = createFilm(filmStorage);
        Film newFilm2 = createFilm(filmStorage);

        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
        User newUser = createUser(userStorage);
        filmStorage.createLike(newUser, newFilm2);

        List<Film> popularFilm2 = new ArrayList<>();
        popularFilm2.add(filmStorage.findById(newFilm2.getId()).get());

        assertEquals(popularFilm2, filmStorage.getPopular(1));
    }

    @Test
    public void testDeleteLike() {
        FilmStorage filmStorage = new FilmDbStorage(namedParameterJdbcTemplate);
        Film newFilm = createFilm(filmStorage);
        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
        User newUser = createUser(userStorage);

        filmStorage.createLike(newUser, newFilm);
        filmStorage.deleteLike(newUser, newFilm);

        Optional<Film> film = filmStorage.findById(newFilm.getId());

        assertEquals(0, film.get().getRate());
    }
}
