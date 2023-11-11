package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final LinkedHashMap<Integer, User> users = new LinkedHashMap<>();
    private int nextId = 1;

    @Override
    public User add(User user) {
        user.setId(nextId);
        nextId += 1;
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Integer idUser) {
        users.remove(idUser);
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Ошибка");
        }
        users.put(user.getId(), user);
        return user;
    }

    public List<User> getAll() {
        return users.values().stream().collect(Collectors.toList());
    }

    public User findById(Integer id) {
        if (users.get(id) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        return users.get(id);
    }

    public void addFriends(User user, User friend) {
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void deleteFriends(User user, User friend) {
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public List<User> findAllFriends(Integer idUser) {
        List<User> friends = new ArrayList<>();
        User user = findById(idUser);
        if (user.getFriends() != null) {
            for (Integer id : user.getFriends()) {
                friends.add(findById(id));
            }
        }
        return friends;
    }

    public List<User> findCommonFriends(User user, User otherUser) {
        List<User> commonFriends = new ArrayList<>();
        for (Integer friend : user.getFriends()) {
            if (otherUser.getFriends().contains(friend)) {
                commonFriends.add(findById(friend));
            }
        }
        return commonFriends;
    }
}
