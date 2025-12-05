package com.care.hub.data.repositories;

import com.care.hub.data.entities.Schedule;
import com.care.hub.data.rowmapper.ScheduleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleJdbcRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public Schedule save(Schedule schedule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
                INSERT INTO schedules (doctor_id, patient_id, schedule_date, schedule_hour, observation, status)
                VALUES (:doctor_id, :patient_id, :schedule_date, :schedule_hour, :observation, :status)
                """)
                .param("doctor_id", schedule.getDoctorId())
                .param("patient_id", schedule.getPatientId())
                .param("schedule_date", schedule.getScheduleDate() != null ? Date.valueOf(schedule.getScheduleDate()) : null)
                .param("schedule_hour", schedule.getScheduleHour() != null ? Time.valueOf(schedule.getScheduleHour()) : null)
                .param("observation", schedule.getObservation())
                .param("status", schedule.getStatus())
                .update(keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            schedule.setId(generatedId.longValue());
        }
        return schedule;
    }

    public Optional<Schedule> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT id, doctor_id, patient_id, schedule_date, schedule_hour, observation, status
                        FROM schedules
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(new ScheduleRowMapper())
                .optional();
    }

    public List<Schedule> findAll(int page, int perPage) {
        int offset = page * perPage;
        return jdbcClient.sql("""
                        SELECT id, doctor_id, patient_id, schedule_date, schedule_hour, observation, status
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
                            schedule_hour = :schedule_hour,
                            observation = :observation,
                            status = :status
                        WHERE id = :id
                        """)
                .param("schedule_date", schedule.getScheduleDate() != null ? Date.valueOf(schedule.getScheduleDate()) : null)
                .param("schedule_hour", schedule.getScheduleHour() != null ? Time.valueOf(schedule.getScheduleHour()) : null)
                .param("observation", schedule.getObservation())
                .param("status", schedule.getStatus())
                .param("id", schedule.getId())
                .update();
    }

    public void deleteById(Long id) {
        jdbcClient.sql("DELETE FROM schedules WHERE id = :id")
                .param("id", id)
                .update();
    }
}
