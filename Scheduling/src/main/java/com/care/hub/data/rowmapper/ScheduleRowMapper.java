package com.care.hub.data.rowmapper;

import com.care.hub.data.entities.Schedule;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ScheduleRowMapper implements RowMapper<Schedule> {
    @Override
    public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
        var schedule = new Schedule();

        schedule.setId(rs.getLong("nr_seq_schedule"));
        schedule.setDoctorId(rs.getLong("nr_seq_doctor"));
        schedule.setPatientId(rs.getLong("nr_seq_patient"));

        Timestamp ts = rs.getTimestamp("schedule_date");
        schedule.setScheduleDate(ts != null ? ts.toLocalDateTime() : null);

        schedule.setObservation(rs.getString("observation"));
        schedule.setStatus(rs.getString("status"));

        return schedule;
    }
}
