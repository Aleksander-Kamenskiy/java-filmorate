
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
        User user = User.builder()
                .email("@Email")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();
        userValidator.validate(user);
    }

    @Test
    public void userEmailTest() throws ValidationException {
        User user = User.builder()
                .email("")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    public void userEmailTest2() throws ValidationException {
        User user = User.builder()
                .email("email")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    public void userLoginTest() throws ValidationException {
        User user = User.builder()
                .email("@email")
                .login("")
                .name("name")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    public void userLoginTest2() throws ValidationException {
        User user = User.builder()
                .email("@email")
                .login(" lgn ")
                .name("name")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    public void createNameTest() throws ValidationException {
        User user = User.builder()
                .email("@Email")
                .login("login")
                .name("")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();
        userValidator.validate(user);
    }

    @Test
    public void userBirthdayTest() throws ValidationException {
        User user = User.builder()
                .email("@Email")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }
}
