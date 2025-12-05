package com.care.hub.data.rowmapper;

import com.care.hub.data.entities.Schedule;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleRowMapper implements RowMapper<Schedule> {
    @Override
    public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
        var e = new Schedule();
        e.setId(rs.getLong("id"));
        e.setDoctorId(rs.getLong("doctor_id"));
        e.setPatientId(rs.getLong("patient_id"));
        var date = rs.getDate("schedule_date");
        if (date != null) {
            e.setScheduleDate(date.toLocalDate());
        }
        var time = rs.getTime("schedule_hour");
        if (time != null) {
            e.setScheduleHour(time.toLocalTime());
        }
        e.setObservation(rs.getString("observation"));
        e.setStatus(rs.getString("status"));
        return e;
    }
}
