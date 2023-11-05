package ru.yandex.practicum.filmorate.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;


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
        return userService.getAll();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping(value = "/users/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriends(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public Collection<User> getFriends(@PathVariable Integer id) {
        return userService.findAllFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.findCommonFriends(id, otherId);
    }
}
