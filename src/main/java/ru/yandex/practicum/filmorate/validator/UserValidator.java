package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public void validate(User user) {
        if (user.getLogin().contains(" ") || (user.getLogin().isEmpty())) {
            log.error("неправильный или пустой логин" + user.getId());
            throw new ValidationException("логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("неправильная дата рождения" + user.getId());
            throw new ValidationException("дата рождения");
        }
        if (user.getEmail().isEmpty()) {
            log.error("не заполнена почта " + user.getId());
            throw new ValidationException("почта");
        }
        if (!user.getEmail().contains("@")) {
            log.error("почта без @ " + user.getId());
            throw new ValidationException("почта");
        }
        if (user.getId() == 0) {
            log.error(" id пользователя 0 " + user.getId());
            throw new ValidationException(" id 0");
        }
    }
}