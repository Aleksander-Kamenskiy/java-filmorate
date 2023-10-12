package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public static void validate(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("неправильный логин " + user.getId());
            throw new ValidationException("Есть пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("неправильная дата фильма " + user.getId());
            throw new ValidationException("Ошибка");
        }
    }
}