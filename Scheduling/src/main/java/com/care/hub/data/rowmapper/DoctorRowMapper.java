package com.care.hub.data.rowmapper;

import com.care.hub.data.entities.Doctor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorRowMapper implements RowMapper<Doctor> {
    @Override
    public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
        var e = new Doctor();
        e.setId(rs.getLong("id"));
        e.setName(rs.getString("name"));
        e.setEmail(rs.getString("email"));
        e.setLogin(rs.getString("login"));
        e.setPassword(rs.getString("password"));
        e.setCpf(rs.getString("cpf"));
        e.setCrm(rs.getString("crm"));
        e.setSpeciality(rs.getString("speciality"));
        return e;
    }
}
