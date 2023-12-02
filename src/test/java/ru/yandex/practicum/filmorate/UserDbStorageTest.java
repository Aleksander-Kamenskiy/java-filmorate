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
import java.util.List;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
        User newUser = userStorage.create(user);
        int id = newUser.getId();

        User savedUser = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден: " + id));

        assertEquals(user.getLogin(), savedUser.getLogin());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getBirthday(), savedUser.getBirthday());
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
        User newUser = userStorage.create(user);
        newUser.setName("Kykes name");
        userStorage.update(newUser);
        assertEquals("Kykes name", userStorage.findAll().get(0).getName());
    }


    @Test
    public void testFindAllUsers() {
        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
        User user = User.builder()
                .name("Nick Name1")
                .email("1mail@mail.ru")
                .login("dolore1")
                .birthday(LocalDate.of(2000, 11, 15))
                .build();
        User user1 = userStorage.create(user);
        User newUser = User.builder()
                .name("Nick Name2")
                .email("2mail@mail.ru")
                .login("dolore2")
                .birthday(LocalDate.of(2000, 11, 15))
                .build();
        User user2 = userStorage.create(newUser);
        assertThat(userStorage.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testAddAndDeleteFriend() {
        UserDbStorage userStorage = new UserDbStorage(namedParameterJdbcTemplate);
        User user1 = User.builder()
                .name("Nick")
                .email("1mail@mail.ru")
                .login("1dolore")
                .birthday(LocalDate.of(2001, 11, 15))
                .build();
        User newUser1 = userStorage.create(user1);
        User newUser2 = User.builder()
                .name("Brick")
                .email("2mail@mail.ru")
                .login("2dolore")
                .birthday(LocalDate.of(2002, 11, 15))
                .build();
        User user2 = userStorage.create(newUser2);
        User newUser3 = User.builder()
                .name("Bob")
                .email("3mail@mail.ru")
                .login("3dolore")
                .birthday(LocalDate.of(2003, 11, 15))
                .build();
        User user3 = userStorage.create(newUser3);
        userStorage.findById(newUser1.getId());
        userStorage.findById(newUser2.getId());
        userStorage.findById(newUser3.getId());
        userStorage.addFriend(newUser1, newUser2);
        userStorage.addFriend(newUser3, newUser2);

        assertEquals(1, userStorage.getFriends(user1).size());

        assertThat(userStorage.getCommonFriends(newUser1, newUser3))
                .isEqualTo(List.of(newUser2));

        userStorage.deleteFriend(newUser1, newUser2);

        assertEquals(0, userStorage.getFriends(user1).size());
    }
}
