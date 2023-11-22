package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friends;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Friends> findFriendsByUserIds(Integer id1, Integer id2) {
        final String qs = "SELECT friends_id, user_id_1, user_id_2, friends_status_id " +
                "FROM friends WHERE user_id_1 = ? and user_id_2 = ?";
        final List<Friends> friends = jdbcTemplate
                .query(qs, (rs, rowNum) -> mapRowToFriends(rs), id1, id2);
        return friends.size() > 0 ? Optional.of(friends.get(0)) : Optional.empty();
    }


    @Override
    public Friends create(Friends friends) {
        Map<String, Object> values = new HashMap<>();
        values.put("user_id_1", friends.getUserId1());
        values.put("user_id_2", friends.getUserId2());
        values.put("friends_status_id", friends.getStatusId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("friends")
                .usingGeneratedKeyColumns("friends_id");
        friends.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return friends;
    }

    @Override
    public Friends update(Friends friends) {
        final String qs = "UPDATE friends SET user_id_1 = ?, user_id_2 = ?, friends_status_id = ? " +
                "WHERE friends_id = ?";
        jdbcTemplate.update(qs, friends.getUserId1(), friends.getUserId2(),
                friends.getStatusId(), friends.getId());
        return friends;
    }

    @Override
    public Optional<Friends> delete(Integer id1, Integer id2) {
        Optional<Friends> friends = findFriendsByUserIds(id1, id2);
        if (friends.isPresent()) {
            final String qs = "DELETE FROM friends WHERE friends_id = ?";
            jdbcTemplate.update(qs, friends.get().getId());
        }
        return friends;
    }

    @Override
    public List<Integer> getFriendIdsByUserId(Integer id) {
        final String qs = "SELECT user_id_2 FROM friends WHERE user_id_1 = ?";
        return jdbcTemplate.queryForList(qs, Integer.class, id);
    }

    @Override
    public List<Integer> getCommonFriendIdsByUserIds(Integer id1, Integer id2) {
        final String qs = "SELECT u.user_id " +
                "FROM users u, friends f, friends o " +
                "WHERE u.user_id = f.user_id_2 " +
                "AND u.user_id = o.user_id_2 " +
                "AND f.user_id_1 = ? " +
                "AND o.user_id_1 = ?";
        return jdbcTemplate.queryForList(qs, Integer.class, id1, id2);
    }

    private Friends mapRowToFriends(ResultSet rs) throws SQLException {
        return Friends.builder()
                .id(rs.getInt("friends_id"))
                .userId1(rs.getInt("user_id_1"))
                .userId2(rs.getInt("user_id_2"))
                .statusId(rs.getInt("friends_status_id"))
                .build();
    }
}
