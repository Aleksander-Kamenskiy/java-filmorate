package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public MpaDbStorage(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
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
