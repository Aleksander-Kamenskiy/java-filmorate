package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcUserStorage implements UserStorage {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public JdbcUserStorage(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        final String qs = "SELECT user_id, email, login, name, birthday FROM users;";
        return namedJdbcTemplate.query(qs, (rs, rowNum) -> mapRowToUser(rs));
    }

    @Override
    public Optional<User> findById(Integer id) {
        final String qs = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id = :id;";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        final List<User> users = namedJdbcTemplate.query(qs, parameters, (rs, rowNum) -> mapRowToUser(rs));
        return users.size() > 0 ? Optional.of(users.get(0)) : Optional.empty();
    }

    @Override
    public User create(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedJdbcTemplate.getJdbcTemplate())
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return user;
    }

    @Override
    public void update(User user) {
        final String qs = "UPDATE users SET email = :email, login = :login, name = :name, birthday = :birthday WHERE user_id = :user_id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("email", user.getEmail());
        parameters.addValue("login", user.getLogin());
        parameters.addValue("name", user.getName());
        parameters.addValue("birthday", user.getBirthday());
        parameters.addValue("user_id", user.getId());
        namedJdbcTemplate.update(qs, parameters);
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public void deleteFriend(User user, User friend) {
        final String qs = "DELETE FROM friends WHERE user_id_1 = :user_id_1 AND user_id_2 = :user_id_2";
        MapSqlParameterSource parameters = new MapSqlParameterSource("user_id_1", user.getId());
        parameters.addValue("user_id_2", friend.getId());
        namedJdbcTemplate.update(qs, parameters);
    }

    @Override
    public List<User> getFriends(User user) {
        final String qs = "SELECT user_id_2 FROM friends WHERE user_id_1 = :user_id_1";
        SqlParameterSource parameters = new MapSqlParameterSource("user_id_1", user.getId());
        List<Integer> friendIds = namedJdbcTemplate.queryForList(qs, parameters, Integer.class);
        return getUsers(friendIds);
    }

    private List<User> getUsers(List<Integer> userIds) {
        final String qs = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id IN (:user_ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("user_ids", userIds);
        return namedJdbcTemplate.query(qs, parameters, (rs, rowNum) -> mapRowToUser(rs));
    }


    @Override
    public List<User> getCommonFriends(User user1, User user2) {
        final String qs = "SELECT u.user_id " +
                "FROM users u, friends f, friends o " +
                "WHERE u.user_id = f.user_id_2 " +
                "AND u.user_id = o.user_id_2 " +
                "AND f.user_id_1 = :user_id_1 " +
                "AND o.user_id_1 = :user_id_2";
        MapSqlParameterSource parameters = new MapSqlParameterSource("user_id_1", user1.getId());
        parameters.addValue("user_id_2", user2.getId());
        List<Integer> friendIds = namedJdbcTemplate.queryForList(qs, parameters, Integer.class);
        return getUsers(friendIds);
    }


    @Override
    public void addFriend(User user, User friend) {
        Map<String, Object> values = new HashMap<>();
        values.put("user_id_1", user.getId());
        values.put("user_id_2", friend.getId());
        values.put("friends_status_id", 1);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedJdbcTemplate.getJdbcTemplate())
                .withTableName("friends")
                .usingGeneratedKeyColumns("friends_id");
        simpleJdbcInsert.execute(values);
    }
}