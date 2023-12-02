package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void testFindAllGenre() {

        GenreStorage genreStorage = new GenreDbStorage(namedParameterJdbcTemplate);
        List<Genre> genreList = genreStorage.findAllGenre();
        assertEquals(6,genreList.size());
    }

    @Test
    public void testFindByIdGenre() {

        GenreStorage genreStorage = new GenreDbStorage(namedParameterJdbcTemplate);
        Optional<Genre>  genre = genreStorage.findGenreById(6);
        assertEquals(6,genre.get().getId());
        assertEquals("Боевик",genre.get().getName());
    }
}
