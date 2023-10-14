package ru.yandex.practicum.filmorate.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.LinkedHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final LinkedHashMap<Integer, User> users = new LinkedHashMap<>();
    private int nextId = 1;
    UserValidator validator = new UserValidator();

    @GetMapping("/users")
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        setUserName(user);
        user.setId(nextId);
        validator.validate(user);
        nextId += 1;
        users.put(user.getId(), user);
        log.info("создан пользователь " + user.getId());
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) {
        validator.validate(user);
        setUserName(user);
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Ошибка");
        }
        users.put(user.getId(), user);
        log.info("пользователь обновлен " + user.getId());
        return user;
    }

    private void setUserName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
