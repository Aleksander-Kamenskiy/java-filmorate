package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
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
        User user = findUserById(idUser);
        User friend = findUserById(idFriends);
        user.getFriends().add(idFriends);
        friend.getFriends().add(idUser);
    }

    public void deleteFriends(int idUser, int idFriends) {
        findUserById(idUser).getFriends().remove(idFriends);
        findUserById(idFriends).getFriends().remove(idUser);
    }

    public List<User> findAllFriends(Integer idUser) {
        List<User> friends = new ArrayList<>();
        User user = findUserById(idUser);
        if (user.getFriends() != null) {
            for (Integer id : user.getFriends()) {
                friends.add(findUserById(id));
            }
        }
        return friends;
    }

    public List<User> findCommonFriends(int idUser, int idOther) {
        List<User> commonFriends = new ArrayList<>();
        User user = findUserById(idUser);
        User otherUser = findUserById(idOther);
        for (Integer friend : user.getFriends()) {
            if (otherUser.getFriends().contains(friend)) {
                commonFriends.add(findUserById(friend));
            }
        }
        return commonFriends;
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
