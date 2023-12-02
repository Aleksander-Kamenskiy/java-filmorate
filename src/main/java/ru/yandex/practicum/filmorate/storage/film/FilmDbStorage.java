package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private static ResultSetExtractor<List<Film>> filmExtractor = rs -> {
        List<Film> filmList = new ArrayList<>();
        Film film = null;
        while (rs.next()) {
            int filmId = rs.getInt("film_id");
            if (film == null || filmId != film.getId()) {
                film = mapRowToFilm(rs);
                film.setGenres(new ArrayList<>());
                filmList.add(film);
            }
            if (rs.getInt("genre_id") != 0) {
                film.getGenres().add(GenreMapper.mapRowToGenre(rs));
            }
        }
        return filmList;
    };

    private static Film mapRowToFilm(ResultSet rs) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .rate(rs.getInt("rate"))
                .build();
    }

    @Autowired
    public FilmDbStorage(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        final String qs = "SELECT * FROM films f " +
                "LEFT JOIN rating_mpa m ON m.mpa_id = f.rating_mpa_id " +
                "LEFT JOIN film_genre fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre g ON fg.genre_id = g.genre_id";
        List<Film> filmList = namedJdbcTemplate.query(qs, filmExtractor);
        return filmList;
    }


    @Override
    public Optional<Film> findById(Integer id) {
        final String qs = "SELECT * FROM films f " +
                "LEFT JOIN rating_mpa m ON m.mpa_id = f.rating_mpa_id " +
                "LEFT JOIN film_genre fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        final List<Film> films = namedJdbcTemplate.query(qs, parameters, filmExtractor);
        Optional<Film> film = films.size() > 0 ? Optional.of(films.get(0)) : Optional.empty();
        return film;
    }

    @Override
    public Film createdFilm(Film film) {
        final String qs = "INSERT INTO films (name, description,release_date, duration, rating_mpa_id, rate) " +
                "VALUES (:name, :description, :release_date, :duration, :rating_mpa_id, :rate)";
        MapSqlParameterSource parameters = new MapSqlParameterSource("name", film.getName());
        parameters.addValue("description", film.getDescription());
        parameters.addValue("release_date", film.getReleaseDate());
        parameters.addValue("duration", film.getDuration());
        parameters.addValue("rating_mpa_id", film.getMpa().getId());
        parameters.addValue("rate", 0);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(qs, parameters, keyHolder);

        film.setId(keyHolder.getKey().intValue());
        film.setRate(0);
        filmGenreBatchUpdate(film, film.getGenres());
        return film;
    }

    private void filmGenreBatchUpdate(Film film, List<Genre> genreList) {
        if (genreList == null) {
            film.setGenres(new ArrayList<>());
            return;
        }
        deleteGenreByFilmId(film.getId());
        List<Genre> listWithoutDuplicates = genreList.stream().distinct().collect(Collectors.toList());
        film.setGenres(listWithoutDuplicates);
        namedJdbcTemplate.getJdbcTemplate().batchUpdate(
                "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, listWithoutDuplicates.get(i).getId());
                    }

                    public int getBatchSize() {
                        return listWithoutDuplicates.size();
                    }
                });
    }


    private void deleteGenreByFilmId(Integer filmId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("film_id", filmId);
        namedJdbcTemplate.update("DELETE FROM film_genre WHERE film_id = :film_id", parameters);
    }

    @Override
    public void update(Film film) {
        String qs = "UPDATE films SET name = :name, description = :description, release_date = :release_date, " +
                "duration = :duration, rating_mpa_id = :rating_mpa_id WHERE film_id = :film_id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("name", film.getName());
        parameters.addValue("description", film.getDescription());
        parameters.addValue("release_date", film.getReleaseDate());
        parameters.addValue("duration", film.getDuration());
        parameters.addValue("rating_mpa_id", film.getMpa().getId());
        parameters.addValue("film_id", film.getId());
        namedJdbcTemplate.update(qs, parameters);
        filmGenreBatchUpdate(film, film.getGenres());
    }

    @Override
    public void deleteFilmById(Integer id) {
        deleteGenreByFilmId(id);
        final String qs = "DELETE FROM films WHERE film_id = :film_id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("film_id", id);
        namedJdbcTemplate.update(qs, parameters);
    }


    @Override
    public List<Film> getPopular(int count) {
        final String qs = "SELECT f.film_id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.rate, " +
                "m.mpa_id, " +
                "m.mpa_name, " +
                "fg.genre_id, " +
                "g.genre_name " +
                "FROM films f " +
                "LEFT JOIN rating_mpa m ON m.mpa_id = f.rating_mpa_id " +
                "LEFT JOIN film_genre fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre g ON fg.genre_id = g.genre_id " +
                "ORDER BY f.rate desc " +
                "LIMIT :count";
        MapSqlParameterSource parameters = new MapSqlParameterSource("count", count);
        List<Film> filmList = namedJdbcTemplate.query(qs, parameters, filmExtractor);
        return filmList;
    }


    @Override
    public void createLike(User user, Film film) {
        final String qs = "MERGE INTO likes (user_id, film_id) values (:user_id, :film_id)";
        MapSqlParameterSource parameters = new MapSqlParameterSource("user_id", user.getId());
        parameters.addValue("film_id", film.getId());
        namedJdbcTemplate.update(qs, parameters);
        rateUpdate(film.getId());
    }

    @Override
    public void deleteLike(User user, Film film) {
        final String qs = "DELETE FROM likes WHERE user_id = :user_id and film_id = :film_id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("user_id", user.getId());
        parameters.addValue("film_id", film.getId());
        namedJdbcTemplate.update(qs, parameters);
        rateUpdate(film.getId());
    }


    private void rateUpdate(Integer filmId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("film_id", filmId);
        namedJdbcTemplate.update("UPDATE films f " +
                "SET rate = (" +
                "SELECT COUNT(l.user_id) " +
                "FROM likes l " +
                "WHERE l.film_id = f.film_id" +
                ") " +
                "WHERE film_id = :film_id", parameters);
    }
}