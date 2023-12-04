package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;


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

    @Override
    public Set<Genre> findGenreByIds(Set<Genre> genreSet) {
        List<Integer> genreIds = genreSet.stream().map(Genre::getId).collect(Collectors.toList());
        final String qs = "SELECT genre_id, genre_name FROM genre WHERE genre_id IN (:genre_ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("genre_ids", genreIds);
        return new HashSet<Genre>(namedJdbcTemplate.query(qs, parameters, new GenreRowMapper()));
    }


}
