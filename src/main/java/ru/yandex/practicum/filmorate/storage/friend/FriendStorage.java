package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.Friends;

import java.util.List;
import java.util.Optional;

public interface FriendStorage {
    Optional<Friends> findFriendsByUserIds(Integer id1, Integer id2);

    Friends create(Friends friends);

    Friends update(Friends friends);

    Optional<Friends> delete(Integer id1, Integer id2);

    List<Integer> getFriendIdsByUserId(Integer id);

    List<Integer> getCommonFriendIdsByUserIds(Integer id1, Integer id2);

}