package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();

    Optional<User> findById(Integer id);

    User create(User user);

    void update(User user);

    void deleteFriend(User user, User friend);

    List<User> getFriends(User user);

    List<User> getCommonFriends(User user1, User user2);

    void addFriend(User user, User friend);

}
