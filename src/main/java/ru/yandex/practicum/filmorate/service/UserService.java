package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    UserValidator validator = new UserValidator();

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        setUserName(user);
        validator.validate(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        setUserName(user);
        validator.validateUpdate(user);
        return userStorage.update(user);
    }

    public User getUser(int idUser) {
        return findUserById(idUser);
    }

    public void addFriends(int idUser, int idFriends) {
        userStorage.addFriends(idUser, idFriends);
    }

    public void deleteFriends(int idUser, int idFriends) {
        userStorage.deleteFriends(idUser, idFriends);
    }

    public List<User> findAllFriends(Integer idUser) {
        return userStorage.findAllFriends(idUser);
    }

    public List<User> findCommonFriends(int idUser, int idOther) {
        return userStorage.findCommonFriends(idUser, idOther);
    }

    private User findUserById(int id) {
        User user = userStorage.findById(id);
        if (user == null) {
            throw new NotFoundException(String.format("Нет пользователя с id %s", id));
        }
        return user;
    }

    private void setUserName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
