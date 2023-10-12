package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    @Test
    public void createUserTest() throws ValidationException {
        User user = new User("@Email", "login", "name", LocalDate.of(2010, 10, 10));
        UserController userController = new UserController();
        userController.create(user);
        Collection<User> userCollection = userController.getAll();
        assertEquals(1, userCollection.size());
        User testUser = userCollection.iterator().next();
        assertEquals("@Email", testUser.getEmail());
        assertEquals("login", testUser.getLogin());
        assertEquals("name", testUser.getName());
        assertEquals(LocalDate.of(2010, 10, 10), testUser.getBirthday());
        assertEquals(1, testUser.getId());
    }
}
