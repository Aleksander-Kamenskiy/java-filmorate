package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User add(User user);

    void delete(Integer idUser);

    User update(User user);

    User findById(Integer id);

    void addFriends(User user, User friend);

    void deleteFriends(User user, User friend);

    List<User> findAllFriends(Integer idUser);

    List<User> findCommonFriends(User user, User friend);
}
