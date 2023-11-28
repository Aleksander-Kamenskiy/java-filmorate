package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;


import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;


@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void testFindUserById() {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(1946,8,20))
                .build();
        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
        User newUser = userStorage.create(user);
        int id = newUser.getId();

        User savedUser = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден: " + id));

        assertEquals(user.getLogin(),savedUser.getLogin());
        assertEquals(user.getName(),savedUser.getName());
        assertEquals(user.getEmail(),savedUser.getEmail());
        assertEquals(user.getBirthday(),savedUser.getBirthday());
    }
}
