package com.care.hub.data.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public class ScheduleConflictRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public boolean existsByDoctorAndDateTime(Long doctorId, LocalDate date, LocalTime time) {
        Integer count = jdbcClient.sql("""
                SELECT COUNT(1)
                FROM schedules s
                WHERE s.doctor_id = :doctorId
                  AND s.schedule_date = :date
                  AND s.schedule_hour = :time
                  AND s.status IN ('CONFIRMED','SCHEDULED')
                """)
                .param("doctorId", doctorId)
                .param("date", date)
                .param("time", time)
                .query(Integer.class)
                .single();
        return count > 0;
    }
}
