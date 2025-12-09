package com.care.hub.data.repositories;

import com.care.hub.data.entities.Doctor;
import com.care.hub.data.rowmapper.DoctorRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DoctorJdbcRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public Doctor save(Doctor doctor) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
                INSERT INTO doctors (name, email, crm, speciality, nr_seq_user)
                VALUES (:name, :email, :crm, :speciality, :nr_seq_user)
                """)
                .param("name", doctor.getName())
                .param("email", doctor.getEmail())
                .param("crm", doctor.getCrm())
                .param("speciality", doctor.getSpeciality())
                .param("nr_seq_user", doctor.getUserId())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;

        var id = keys.get("nr_seq_doctor");
        if (id != null) {
            doctor.setId(((Number)id).longValue());
        }

        return doctor;
    }

    public Optional<Doctor> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT nr_seq_doctor, name, email, crm, speciality
                        FROM doctors
                        WHERE nr_seq_doctor = :nr_seq_doctor
                        """)
                .param("nr_seq_doctor", id)
                .query(new DoctorRowMapper())
                .optional();
    }

    public List<Doctor> findAll(int page, int perPage) {
        int offset = page * perPage;
        return jdbcClient.sql("""
                        SELECT nr_seq_doctor, name, email, crm, speciality
                        FROM doctors
                        LIMIT :limit OFFSET :offset
                        """)
                .param("limit", perPage)
                .param("offset", offset)
                .query(new DoctorRowMapper())
                .list();
    }

    public int count() {
        return jdbcClient.sql("SELECT COUNT(*) FROM doctors")
                .query(Integer.class)
                .single();
    }

    public void update(Doctor doctor) {
        jdbcClient.sql("""
                        UPDATE doctors
                        SET name = :name,
                            email = :email,
                            speciality = :speciality
                        WHERE nr_seq_doctor = :nr_seq_doctor
                        """)
                .param("name", doctor.getName())
                .param("email", doctor.getEmail())
                .param("speciality", doctor.getSpeciality())
                .param("nr_seq_doctor", doctor.getId())
                .update();
    }

    public void deleteById(Long id) {
        jdbcClient.sql("DELETE FROM doctors WHERE nr_seq_doctor = :nr_seq_doctor")
                .param("nr_seq_doctor", id)
                .update();
    }
}
