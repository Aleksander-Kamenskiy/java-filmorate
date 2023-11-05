package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    public void createUserTest() throws ValidationException {
        User user = new User("@Email", "login", "name", LocalDate.of(2010, 10, 10));
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

    @Test
    public void userEmailTest() throws ValidationException {
        User user = new User("", "login", "name", LocalDate.of(2010, 10, 10));
        assertThrows(ValidationException.class, () -> userController.create(user));
        Collection<User> userCollection = userController.getAll();
        assertEquals(0, userCollection.size());
    }

    @Test
    public void userEmailTest2() throws ValidationException {
        User user = new User("email", "login", "name", LocalDate.of(2010, 10, 10));
        assertThrows(ValidationException.class, () -> userController.create(user));
        Collection<User> userCollection = userController.getAll();
        assertEquals(0, userCollection.size());
    }

    @Test
    public void userLoginTest() throws ValidationException {
        User user = new User("@email", "", "name", LocalDate.of(2010, 10, 10));
        assertThrows(ValidationException.class, () -> userController.create(user));
        Collection<User> userCollection = userController.getAll();
        assertEquals(0, userCollection.size());
    }

    @Test
    public void userLoginTest2() throws ValidationException {
        User user = new User("@email", " lgn ", "name", LocalDate.of(2010, 10, 10));
        assertThrows(ValidationException.class, () -> userController.create(user));
        Collection<User> userCollection = userController.getAll();
        assertEquals(0, userCollection.size());
    }

    @Test
    public void createNameTest() throws ValidationException {
        User user = new User("@Email", "login", "", LocalDate.of(2010, 10, 10));
        userController.create(user);
        Collection<User> userCollection = userController.getAll();
        assertEquals(1, userCollection.size());
        User testUser = userCollection.iterator().next();
        assertEquals("login", testUser.getLogin());
        assertEquals("login", testUser.getName());
    }

    @Test
    public void userBirthdayTest() throws ValidationException {
        User user = new User("@Email", "login", "name", LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.create(user));
        Collection<User> userCollection = userController.getAll();
        assertEquals(0, userCollection.size());
    }
}
