package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    public void createUserTest() throws ValidationException {
        User user = new User("@Email", "login", "name", LocalDate.of(2010, 10, 10));

        userValidator.validate(user);
    }

    @Test
    public void userEmailTest() throws ValidationException {
        User user = new User("", "login", "name", LocalDate.of(2010, 10, 10));

        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    public void userEmailTest2() throws ValidationException {
        User user = new User("email", "login", "name", LocalDate.of(2010, 10, 10));

        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    public void userLoginTest() throws ValidationException {
        User user = new User("@email", "", "name", LocalDate.of(2010, 10, 10));

        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    public void userLoginTest2() throws ValidationException {
        User user = new User("@email", " lgn ", "name", LocalDate.of(2010, 10, 10));

        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    public void createNameTest() throws ValidationException {
        User user = new User("@Email", "login", "", LocalDate.of(2010, 10, 10));

        userValidator.validate(user);
    }

    @Test
    public void userBirthdayTest() throws ValidationException {
        User user = new User("@Email", "login", "name", LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }
}

