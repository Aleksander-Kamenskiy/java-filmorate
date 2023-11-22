package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Optional;

public interface LikeStorage {

    Optional<Like> findFilmLikeByUserIdFilmId(Integer userId, Integer filmId);

    Optional<Like> createFilmLike(Integer userId, Integer filmId);

    Optional<Like> deleteFilmLike(Integer userId, Integer filmId);

}
