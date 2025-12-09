package com.care.hub.data.rowmapper;

import com.care.hub.data.entities.Patient;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientRowMapper implements RowMapper<Patient> {

    @Override
    public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
        var patient = new Patient();

        patient.setId(rs.getLong("nr_seq_patient"));
        patient.setName(rs.getString("name"));
        patient.setEmail(rs.getString("email"));
        patient.setBirthDate(rs.getDate("birth_date").toLocalDate());
        patient.setAddress(rs.getString("address"));
        patient.setTelephone(rs.getString("telephone"));

        return patient;
    }
}
