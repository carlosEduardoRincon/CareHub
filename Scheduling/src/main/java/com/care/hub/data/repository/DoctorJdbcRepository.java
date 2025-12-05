package com.care.hub.data.repository;

import com.care.hub.data.entities.Doctor;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class DoctorJdbcRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void save(Doctor doctor) {
//        String sql = "INSERT INTO medicos (crm, especialidade, user_id) VALUES (?, ?, ?)";
//        jdbc.update(sql,
//                doctor.getCrm(),
//                doctor.getSpeciality(),
//                doctor.getUserId());
    }
}
