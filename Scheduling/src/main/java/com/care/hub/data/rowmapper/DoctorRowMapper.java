package com.care.hub.data.rowmapper;

import com.care.hub.data.entities.Doctor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorRowMapper implements RowMapper<Doctor> {

    @Override
    public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
        var doctor = new Doctor();

        doctor.setId(rs.getLong("nr_seq_doctor"));
        doctor.setName(rs.getString("name"));
        doctor.setEmail(rs.getString("email"));
        doctor.setCrm(rs.getString("crm"));
        doctor.setSpeciality(rs.getString("speciality"));

        return doctor;
    }
}
