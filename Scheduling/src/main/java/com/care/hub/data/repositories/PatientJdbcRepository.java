package com.care.hub.data.repositories;

import com.care.hub.data.entities.Patient;
import com.care.hub.data.rowmapper.PatientRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientJdbcRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public Patient save(Patient patient) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                INSERT INTO patients (name, email, birth_date, address, telephone, nr_seq_user)
                VALUES (:name, :email, :birth_date, :address, :telephone, :nr_seq_user)
                """)
                .param("name", patient.getName())
                .param("email", patient.getEmail())
                .param("birth_date", Date.valueOf(patient.getBirthDate()))
                .param("address", patient.getAddress())
                .param("telephone", patient.getTelephone())
                .param("nr_seq_user", patient.getUserId())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;

        var id = keys.get("nr_seq_patient");
        if (id != null) {
            patient.setId(((Number)id).longValue());
        }

        return patient;
    }

    public Optional<Patient> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT nr_seq_patient, name, email, birth_date, address, telephone
                        FROM patients
                        WHERE nr_seq_patient = :nr_seq_patient
                        """)
                .param("nr_seq_patient", id)
                .query(new PatientRowMapper())
                .optional();
    }

    public List<Patient> findAll(int page, int perPage) {
        int offset = page * perPage;
        return jdbcClient.sql("""
                        SELECT nr_seq_patient, name, email, birth_date, address, telephone
                        FROM patients
                        LIMIT :limit OFFSET :offset
                        """)
                .param("limit", perPage)
                .param("offset", offset)
                .query(new PatientRowMapper())
                .list();
    }

    public int count() {
        return jdbcClient.sql("SELECT COUNT(*) FROM patients")
                .query(Integer.class)
                .single();
    }

    public void update(Patient patient) {
        jdbcClient.sql("""
                        UPDATE patients
                        SET name = :name,
                            email = :email,
                            birth_date = :birth_date,
                            address = :address,
                            telephone = :telephone
                        WHERE nr_seq_patient = :nr_seq_patient
                        """)
                .param("name", patient.getName())
                .param("email", patient.getEmail())
                .param("birth_date", Date.valueOf(patient.getBirthDate()))
                .param("address", patient.getAddress())
                .param("telephone", patient.getTelephone())
                .param("nr_seq_patient", patient.getId())
                .update();
    }

    public void deleteById(Long id) {
        jdbcClient.sql("DELETE FROM patients WHERE nr_seq_patient = :nr_seq_patient")
                .param("nr_seq_patient", id)
                .update();
    }
}
