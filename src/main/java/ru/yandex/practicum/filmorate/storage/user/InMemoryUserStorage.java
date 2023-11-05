package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final LinkedHashMap<Integer, User> users = new LinkedHashMap<>();
    private int nextId = 1;
    UserValidator validator = new UserValidator();


    private boolean checkValidationUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        return true;
    }

    private void setUserName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public User addUser(User user) {
        setUserName(user);
        user.setId(nextId);
        validator.validate(user);
        nextId += 1;
        users.put(user.getId(), user);
        log.info("создан пользователь " + user.getId());
        return user;
    }

    @Override
    public void deleteUser(Integer idUser) {
        users.remove(idUser);
    }

    @Override
    public User changeUser(User user) {
        validator.validateUpdate(user);
        setUserName(user);
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Ошибка");
        }
        users.put(user.getId(), user);
        log.info("пользователь обновлен " + user.getId());
        return user;
    }

    public List<User> getAllUsers() {
        return users.values().stream().collect(Collectors.toList());
    }

    public User findUserById(Integer id) {
        if (users.get(id) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        return users.get(id);
    }
}
