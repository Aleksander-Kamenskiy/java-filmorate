package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.JdbcMpaStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcMpaStorageTest {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void testFindAllMpa() {

        MpaStorage mpaStorage = new JdbcMpaStorage(namedParameterJdbcTemplate);
        List<Mpa> mpaList = mpaStorage.findAllMpa();
        assertEquals(5,mpaList.size());
    }

    @Test
    public void testFindByIdMpa() {

        MpaStorage mpaStorage = new JdbcMpaStorage(namedParameterJdbcTemplate);
        Optional<Mpa> mpa = mpaStorage.findMpaById(2);
        assertEquals(2,mpa.get().getId());
        assertEquals("PG",mpa.get().getName());
    }
}
