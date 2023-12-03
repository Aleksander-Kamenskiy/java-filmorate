package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;


@Repository
public class JdbcGenreStorage implements GenreStorage {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public JdbcGenreStorage(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public List<Genre> findAllGenre() {
        final String qs = "SELECT genre_id, genre_name FROM genre";
        return namedJdbcTemplate.query(qs, new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        final String qs = "SELECT genre_id, genre_name FROM genre WHERE genre_id = :genre_id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("genre_id", id);
        final List<Genre> genres = namedJdbcTemplate.query(qs, parameters, new GenreRowMapper());
        return genres.size() > 0 ? Optional.of(genres.get(0)) : Optional.empty();
    }
}
