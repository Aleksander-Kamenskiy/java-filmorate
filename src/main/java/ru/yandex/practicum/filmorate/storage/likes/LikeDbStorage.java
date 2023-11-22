package ru.yandex.practicum.filmorate.storage.likes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;
import java.util.Optional;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Like> findFilmLikeByUserIdFilmId(Integer userId, Integer filmId) {
        final String qs = "SELECT user_id, film_id " +
                "FROM likes WHERE user_id = ? and film_id = ?";
        final List<Like> filmLike = jdbcTemplate
                .query(qs, (rs, rowNum) -> mapRowToFilmLike(rs), userId, filmId);
        return filmLike.size() > 0 ? Optional.of(filmLike.get(0)) : Optional.empty();
    }

    @Override
    public Optional<Like> createFilmLike(Integer userId, Integer filmId) {
        final String qs = "INSERT INTO likes (user_id, film_id) values (?, ?)";
        jdbcTemplate.update(qs, userId, filmId);
        rateUpdate(filmId);
        return findFilmLikeByUserIdFilmId(userId, filmId);
    }

    @Override
    public Optional<Like> deleteFilmLike(Integer userId, Integer filmId) {
        Optional<Like> filmLike = findFilmLikeByUserIdFilmId(userId, filmId);
        if (filmLike.isPresent()) {
            final String qs = "DELETE FROM likes WHERE user_id = ? and film_id = ?";
            jdbcTemplate.update(qs, userId, filmId);
            rateUpdate(filmId);
        }
        return filmLike;
    }

    private Like mapRowToFilmLike(ResultSet rs) throws SQLException {
        return Like.builder()
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .build();
    }

    private void rateUpdate(Integer filmId) {
        jdbcTemplate.update("UPDATE films f " +
                "SET rate = (" +
                "SELECT COUNT(l.user_id) " +
                "FROM likes l " +
                "WHERE l.film_id = f.film_id" +
                ") " +
                "WHERE film_id = ?", filmId);
    }


}
