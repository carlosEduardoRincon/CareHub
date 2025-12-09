package com.care.hub.data.rowmapper;

import com.care.hub.data.entities.Nurse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NurseRowMapper implements RowMapper<Nurse> {
    @Override
    public Nurse mapRow(ResultSet rs, int rowNum) throws SQLException {
        var nurse = new Nurse();

        nurse.setId(rs.getLong("nr_seq_nurse"));
        nurse.setName(rs.getString("name"));
        nurse.setCoren(rs.getString("coren"));

        return nurse;
    }
}
