package com.care.hub.data.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Repository
public class ScheduleConflictRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public boolean existsByDoctorAndDateTime(Long doctorId, LocalDateTime date) {
        Integer count = jdbcClient.sql("""
                SELECT COUNT(1)
                FROM schedules s
                WHERE s.nr_seq_doctor = :nr_seq_doctor
                  AND s.schedule_date = :date
                  AND s.status IN ('SCHEDULED')
                """)
                .param("nr_seq_doctor", doctorId)
                .param("date", date)
                .query(Integer.class)
                .single();
        return count > 0;
    }
}
