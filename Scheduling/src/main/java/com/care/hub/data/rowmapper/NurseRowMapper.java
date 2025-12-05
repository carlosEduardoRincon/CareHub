package com.care.hub.data.rowmapper;

import com.care.hub.data.entities.Nurse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NurseRowMapper implements RowMapper<Nurse> {
    @Override
    public Nurse mapRow(ResultSet rs, int rowNum) throws SQLException {
        var e = new Nurse();
        e.setId(rs.getLong("id"));
        e.setName(rs.getString("name"));
        e.setLogin(rs.getString("login"));
        e.setPassword(rs.getString("password"));
        e.setCpf(rs.getString("cpf"));
        e.setCoren(rs.getString("coren"));
        return e;
    }
}
