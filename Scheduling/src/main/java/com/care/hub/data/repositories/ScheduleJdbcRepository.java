package com.care.hub.data.repositories;

import com.care.hub.data.entities.Schedule;
import com.care.hub.data.rowmapper.ScheduleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleJdbcRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public Schedule save(Schedule schedule) {
        boolean doctorExists = jdbcClient.sql("SELECT COUNT(*) FROM doctors WHERE nr_seq_doctor = :nr_seq_doctor")
                .param("nr_seq_doctor", schedule.getDoctorId())
                .query(Integer.class)
                .single() > 0;
        if (!doctorExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado.");
        }

        boolean patientExists = jdbcClient.sql("SELECT COUNT(*) FROM patients WHERE nr_seq_patient = :nr_seq_patient")
                .param("nr_seq_patient", schedule.getPatientId())
                .query(Integer.class)
                .single() > 0;
        if (!patientExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado.");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
                INSERT INTO schedules (nr_seq_doctor, nr_seq_patient, schedule_date, observation, status)
                VALUES (:nr_seq_doctor, :nr_seq_patient, :schedule_date, :observation, :status)
                """)
                .param("nr_seq_doctor", schedule.getDoctorId())
                .param("nr_seq_patient", schedule.getPatientId())
                .param("schedule_date", schedule.getScheduleDate())
                .param("observation", schedule.getObservation())
                .param("status", schedule.getStatus())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;

        var id = keys.get("nr_seq_schedule");
        if (id != null) {
            schedule.setId(((Number)id).longValue());
        }

        return schedule;
    }

    public Optional<Schedule> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT nr_seq_schedule, nr_seq_doctor, nr_seq_patient, schedule_date, observation, status
                        FROM schedules
                        WHERE nr_seq_schedule = :nr_seq_schedule
                        """)
                .param("nr_seq_schedule", id)
                .query(new ScheduleRowMapper())
                .optional();
    }

    public List<Schedule> findAll(int page, int perPage) {
        int offset = page * perPage;
        return jdbcClient.sql("""
                        SELECT nr_seq_schedule, nr_seq_doctor, nr_seq_patient, schedule_date, observation, status
                        FROM schedules
                        LIMIT :limit OFFSET :offset
                        """)
                .param("limit", perPage)
                .param("offset", offset)
                .query(new ScheduleRowMapper())
                .list();
    }

    public int count() {
        return jdbcClient.sql("SELECT COUNT(*) FROM schedules")
                .query(Integer.class)
                .single();
    }

    public void update(Schedule schedule) {
        jdbcClient.sql("""
                        UPDATE schedules
                        SET schedule_date = :schedule_date,
                            observation = :observation,
                            status = :status
                        WHERE nr_seq_schedule = :nr_seq_schedule
                        """)
                .param("schedule_date", schedule.getScheduleDate())
                .param("observation", schedule.getObservation())
                .param("status", schedule.getStatus())
                .param("nr_seq_schedule", schedule.getId())
                .update();
    }

    public void deleteById(Long id) {
        jdbcClient.sql("DELETE FROM schedules WHERE nr_seq_schedule = :nr_seq_schedule")
                .param("nr_seq_schedule", id)
                .update();
    }
}
