package com.care.hub.data;

import com.care.hub.model.ScheduleNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class NotificationScheduleRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public List<ScheduleNotification> findUpcomingWithin(LocalDate date) {
        return jdbcClient.sql("""
                        SELECT s.nr_seq_schedule,
                               s.schedule_date,
                               s.observation,
                               p.email AS patient_email,
                               p.name  AS patient_name,
                               d.email AS doctor_email,
                               d.name  AS doctor_name
                        FROM schedules s
                        JOIN patients p ON p.nr_seq_patient = s.nr_seq_patient
                        JOIN doctors d ON d.nr_seq_doctor = s.nr_seq_doctor
                        WHERE s.schedule_date = :date
                          AND s.status IN ('CONFIRMED','SCHEDULED')
                        """)
                .param("date", date)
                .query(new ScheduleNotificationRowMapper())
                .list();
    }

    public Optional<ScheduleNotification> findByScheduleId(Long scheduleId) {
        return jdbcClient.sql("""
                        SELECT s.nr_seq_schedule,
                               s.schedule_date,
                               s.observation,
                               p.email AS patient_email,
                               p.name  AS patient_name,
                               d.email AS doctor_email,
                               d.name  AS doctor_name
                        FROM schedules s
                        JOIN patients p ON p.nr_seq_patient = s.nr_seq_patient
                        JOIN doctors d ON d.nr_seq_doctor = s.nr_seq_doctor
                        WHERE s.nr_seq_schedule = :nr_seq_schedule
                        """)
                .param("nr_seq_schedule", scheduleId)
                .query(new ScheduleNotificationRowMapper())
                .optional();
    }

    static class ScheduleNotificationRowMapper implements RowMapper<ScheduleNotification> {
        @Override
        public ScheduleNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
            var scheduleNotification = new ScheduleNotification();

            scheduleNotification.setScheduleId(rs.getLong("nr_seq_schedule"));
            scheduleNotification.setDate(rs.getTimestamp("schedule_date").toLocalDateTime());
            scheduleNotification.setObservation(rs.getString("observation"));
            scheduleNotification.setPatientEmail(rs.getString("patient_email"));
            scheduleNotification.setPatientName(rs.getString("patient_name"));
            scheduleNotification.setDoctorEmail(rs.getString("doctor_email"));
            scheduleNotification.setDoctorName(rs.getString("doctor_name"));

            return scheduleNotification;
        }
    }
}
