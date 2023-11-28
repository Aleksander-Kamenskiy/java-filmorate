package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public FilmDbStorage(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        final String qs = "SELECT * FROM films f, rating_mpa m WHERE m.mpa_id = f.rating_mpa_id";
        List<Film> filmList = namedJdbcTemplate.query(qs, (rs, rowNum) -> mapRowToFilm(rs));
        filmList.forEach(film -> film.setGenres(findGenresByFilmId(film.getId())));
        return filmList;
    }

    @Override
    public Optional<Film> findById(Integer id) {
        final String qs = "SELECT * FROM films f, rating_mpa m WHERE m.mpa_id = f.rating_mpa_id AND f.film_id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        final List<Film> films = namedJdbcTemplate.query(qs, parameters, (rs, rowNum) -> mapRowToFilm(rs));
        Optional<Film> film = films.size() > 0 ? Optional.of(films.get(0)) : Optional.empty();
        if (film.isPresent()) {
            film.get().setGenres(findGenresByFilmId(film.get().getId()));
        }
        return film;
    }

    @Override
    public Film createdFilm(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", Date.valueOf(film.getReleaseDate()));
        values.put("duration", film.getDuration());
        values.put("rating_mpa_id", film.getMpa().getId());
        values.put("rate", 0);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedJdbcTemplate.getJdbcTemplate())
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        filmGenreBatchUpdate(film.getId(), film.getGenres());
        film.setGenres(findGenresByFilmId(film.getId()));
        return film;
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
        filmGenreBatchUpdate(film.getId(), film.getGenres());
        film.setGenres(findGenresByFilmId(film.getId()));
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
        final String qs = "SELECT f.film_id," +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_id, " +
                "m.mpa_name " +
                "FROM films f " +
                "LEFT JOIN rating_mpa m ON m.mpa_id = f.rating_mpa_id " +
                "ORDER BY f.rate desc " +
                "LIMIT :count";
        MapSqlParameterSource parameters = new MapSqlParameterSource("count", count);
        List<Film> filmList = namedJdbcTemplate.query(qs, parameters, (rs, rowNum) -> mapRowToFilm(rs));
        filmList.forEach(film -> film.setGenres(findGenresByFilmId(film.getId())));
        return filmList;
    }

    private Film mapRowToFilm(ResultSet rs) throws SQLException {
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
                .build();
    }

    @Override
    public void createLike(User user, Film film) {
        final String qs = "INSERT INTO likes (user_id, film_id) values (:user_id, :film_id)";
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

    @Override
    public List<Genre> findAllGenre() {
        final String qs = "SELECT genre_id, genre_name FROM genre";
        return namedJdbcTemplate.query(qs, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        final String qs = "SELECT genre_id, genre_name FROM genre WHERE genre_id = :genre_id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("genre_id", id);
        final List<Genre> genres = namedJdbcTemplate.query(qs, parameters, (rs, rowNum) -> mapRowToGenre(rs));
        return genres.size() > 0 ? Optional.of(genres.get(0)) : Optional.empty();
    }

    @Override
    public List<Genre> findGenresByFilmId(Integer filmId) {
        final String qs = "SELECT g.genre_id, g.genre_name " +
                "FROM genre g " +
                "INNER JOIN film_genre fg on g.genre_id = fg.genre_id " +
                "WHERE film_id = :film_id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("film_id", filmId);
        return namedJdbcTemplate.query(qs, parameters, (rs, rowNum) -> mapRowToGenre(rs));
    }

    public void filmGenreBatchUpdate(Integer filmId, List<Genre> genreList) {
        if (genreList == null) {
            return;
        }
        deleteGenreByFilmId(filmId);
        List<Genre> listWithoutDuplicates = genreList.stream().distinct().collect(Collectors.toList());
        namedJdbcTemplate.getJdbcTemplate().batchUpdate(
                "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, listWithoutDuplicates.get(i).getId());
                    }

                    public int getBatchSize() {
                        return listWithoutDuplicates.size();
                    }
                });
    }

    @Override
    public void deleteGenreByFilmId(Integer filmId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("film_id", filmId);
        namedJdbcTemplate.update("DELETE FROM film_genre WHERE film_id = :film_id", parameters);
    }

    private Genre mapRowToGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

    @Override
    public List<Mpa> findAllMpa() {
        final String qs = "SELECT mpa_id, mpa_name FROM rating_mpa";
        return namedJdbcTemplate.query(qs, (rs, rowNum) -> mapRowToMpa(rs));
    }

    @Override
    public Optional<Mpa> findMpaById(Integer id) {
        final String qs = "SELECT mpa_id, mpa_name FROM rating_mpa WHERE mpa_id = :mpa_id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("mpa_id", id);
        final List<Mpa> mpas = namedJdbcTemplate.query(qs, parameters, (rs, rowNum) -> mapRowToMpa(rs));
        return mpas.size() > 0 ? Optional.of(mpas.get(0)) : Optional.empty();
    }

    private Mpa mapRowToMpa(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }
}