package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    UserValidator validator = new UserValidator();

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Integer id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public User create(User user) {
        setUserName(user);
        validator.validate(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        setUserName(user);
        validator.validateUpdate(user);
        findById(user.getId());
        userStorage.update(user);
        return user;
    }

    public void addFriend(Integer id, Integer friendId) {
        User user = findById(id);
        User friend = findById(friendId);
        userStorage.addFriend(user, friend);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        User user = findById(id);
        User friend = findById(friendId);
        userStorage.deleteFriend(user, friend);
    }

    public List<User> getFriends(Integer id) {
        User user = findById(id);
        return userStorage.getFriends(user);
    }

    public List<User> getCommonFriends(Integer id, Integer friendId) {
        User user = findById(id);
        User friend = findById(friendId);
        return userStorage.getCommonFriends(user, friend);
    }

    private void setUserName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}