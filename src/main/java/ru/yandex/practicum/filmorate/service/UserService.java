package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Friends;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    UserValidator validator = new UserValidator();

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Integer id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));
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
        return userStorage.update(user);
    }

    public Friends addFriend(Integer id, Integer friendId) {
        findById(id);
        findById(friendId);
        Optional<Friends> friends = friendStorage.findFriendsByUserIds(id, friendId);
        return friends.orElseGet(() -> friendStorage.create(Friends.builder()
                .id(0)
                .userId1(id)
                .userId2(friendId)
                .statusId(1)
                .build()));
    }

    public Friends deleteFriend(Integer id, Integer friendId) {
        findById(id);
        findById(friendId);
        friendStorage.findFriendsByUserIds(id, friendId);
        return friendStorage.delete(id, friendId).orElseThrow(() -> new NotFoundException("Friendsdoes not exist"));
    }

    public List<User> getFriends(Integer id) {
        findById(id);
        return friendStorage.getFriendIdsByUserId(id).stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer friendId) {
        findById(id);
        findById(friendId);
        return friendStorage.getCommonFriendIdsByUserIds(id, friendId).stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    private void setUserName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}