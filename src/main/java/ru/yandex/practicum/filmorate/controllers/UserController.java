package ru.yandex.practicum.filmorate.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Collection<User> getAll() {
        log.info("Получение пользователей");
        Collection<User> getAllUser = userService.getAll();
        log.info("Пользователи получены");
        return getAllUser;
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        log.info("Создание пользователя");
        User userCreate = userService.create(user);
        log.info("Пользователь создан");
        return userCreate;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) {
        log.info("Обновление пользователя");
        User updateUser = userService.update(user);
        log.info("Пользователь обновлен");
        return updateUser;
    }

    @GetMapping(value = "/users/{id}")
    public User getUser(@PathVariable Integer id) {
        log.info("Получение пользователя");
        User getUser1 = userService.getUser(id);
        log.info("Пользователь получен");
        return getUser1;
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Добавление друга");
        userService.addFriends(id, friendId);
        log.info("Друг добавлен");
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Удаление друга");
        userService.deleteFriends(id, friendId);
        log.info("Друг удален");
    }

    @GetMapping(value = "/users/{id}/friends")
    public Collection<User> getFriends(@PathVariable Integer id) {
        log.info("Получение друзей");
        List<User> getFriends1 = userService.findAllFriends(id);
        log.info("Друзья получены");
        return getFriends1;
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Получение общих друзей");
        List<User> commonFriends = userService.findCommonFriends(id, otherId);
        log.info("Общие друзья получены");
        return commonFriends;
    }
}
