package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAll() {
        return userStorage.getAllUsers();
    }

    public User create(User user) {
        return userStorage.addUser(user);
    }

    public User update(User user) {
        return userStorage.changeUser(user);
    }

    public User getUser(int idUser) {
        return userStorage.findUserById(idUser);
    }

    public void addFriends(int idUser, int idFriends) {
        User user = userStorage.findUserById(idUser);
        User friend = userStorage.findUserById(idFriends);
        user.getFriends().add(idFriends);
        friend.getFriends().add(idUser);
    }

    public void deleteFriends(int idUser, int idFriends) {
        userStorage.findUserById(idUser).getFriends().remove(idFriends);
        userStorage.findUserById(idFriends).getFriends().remove(idUser);
    }

    public List<User> findAllFriends(Integer idUser) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.findUserById(idUser);
        if (user.getFriends() != null) {
            for (Integer id : user.getFriends()) {
                friends.add(userStorage.findUserById(id));
            }
        }
        return friends;
    }

    public List<User> findCommonFriends(int idUser, int idOther) {
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.findUserById(idUser);
        User otherUser = userStorage.findUserById(idOther);
        for (Integer friend : user.getFriends()) {
            if (otherUser.getFriends().contains(friend)) {
                commonFriends.add(userStorage.findUserById(friend));
            }
        }
        return commonFriends;
    }
}
