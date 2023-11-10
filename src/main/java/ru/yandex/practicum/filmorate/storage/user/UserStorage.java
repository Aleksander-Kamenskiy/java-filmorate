package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User add(User user);

    void delete(Integer idUser);

    User update(User user);

    User findById(Integer id);

    void addFriends(int idUser, int idFriends);

    void deleteFriends(int idUser, int idFriends);

    List<User> findAllFriends(Integer idUser);

    List<User> findCommonFriends(int idUser, int idOther);
}
