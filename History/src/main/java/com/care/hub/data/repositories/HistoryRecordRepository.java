package com.care.hub.data.repositories;

import com.care.hub.data.entities.HistoryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class HistoryRecordRepository {

    @Autowired
    private JdbcClient jdbcClient;

    public HistoryRecord save(HistoryRecord entity) {
        if (entity.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcClient.sql("""
                            INSERT INTO history_records
                                (nr_seq_patient, nr_seq_schedule, nr_seq_doctor, status, schedule_date, payload, created_at)
                            VALUES
                                (:patientId, :scheduleId, :doctorId, :eventType, :eventTime, :payload, :created_at)
                            """)
                    .param("patientId", entity.getPatientId())
                    .param("scheduleId", entity.getScheduleId())
                    .param("doctorId", entity.getDoctorId())
                    .param("eventType", entity.getEventType())
                    .param("eventTime", entity.getEventTime())
                    .param("payload", entity.getPayload())
                    .param("created_at", LocalDateTime.now())
                    .update(keyHolder);

            var keys = keyHolder.getKeys();
            Objects.requireNonNull(keys, "Failed to obtain generated keys when inserting into history_records");
            Object rawId = keys.get("nr_seq_history_record");
            if (rawId == null && keys.size() == 1) {
                rawId = keys.values().iterator().next();
            }
            if (!(rawId instanceof Number)) {
                throw new IllegalStateException("Invalid generated key nr_seq_history_record: " + rawId);
            }
            entity.setId(((Number) rawId).longValue());

            return findById(entity.getId()).orElse(entity);
        } else {
            jdbcClient.sql("""
                            UPDATE history_records
                            SET status = COALESCE(:eventType, status),
                                schedule_date = COALESCE(:eventTime, schedule_date),
                                payload    = COALESCE(:payload, payload),
                                nr_seq_schedule = COALESCE(:scheduleId, nr_seq_schedule)
                            WHERE nr_seq_history_record = :id
                            """)
                    .param("eventType", entity.getEventType())
                    .param("eventTime", entity.getEventTime())
                    .param("payload", entity.getPayload())
                    .param("scheduleId", entity.getScheduleId())
                    .param("id", entity.getId())
                    .update();

            return findById(entity.getId()).orElse(entity);
        }
    }

    public List<HistoryRecord> findByPatientIdOrderByEventTimeAsc(Long patientId) {
        return jdbcClient.sql("""
                        SELECT
                            nr_seq_history_record,
                            nr_seq_patient,
                            nr_seq_schedule,
                            nr_seq_doctor,
                            status,
                            schedule_date,
                            payload,
                            created_at
                        FROM history_records
                        WHERE nr_seq_patient = :patientId
                        ORDER BY schedule_date ASC
                        """)
                .param("patientId", patientId)
                .query(this::mapRow)
                .list();
    }

    public List<HistoryRecord> findByPatientIdAndEventTimeAfterOrderByEventTimeAsc(Long patientId, Instant eventTimeAfter) {
        return jdbcClient.sql("""
                        SELECT
                            nr_seq_history_record,
                            nr_seq_patient,
                            nr_seq_schedule,
                            nr_seq_doctor,
                            status,
                            schedule_date,
                            payload,
                            created_at
                        FROM history_records
                        WHERE nr_seq_patient = :patientId
                          AND schedule_date > :eventTimeAfter
                        ORDER BY schedule_date ASC
                        """)
                .param("patientId", patientId)
                .param("eventTimeAfter", eventTimeAfter)
                .query(this::mapRow)
                .list();
    }

    public Optional<HistoryRecord> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT
                            nr_seq_history_record,
                            nr_seq_patient,
                            nr_seq_schedule,
                            nr_seq_doctor,
                            status,
                            schedule_date,
                            payload,
                            created_at
                        FROM history_records
                        WHERE nr_seq_history_record = :id
                        """)
                .param("id", id)
                .query(this::mapRow)
                .optional();
    }

    private HistoryRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        HistoryRecord hr = new HistoryRecord();
        Long id = (Long) rs.getObject("nr_seq_history_record");
        Long patientId = (Long) rs.getObject("nr_seq_patient");
        Long scheduleId = (Long) rs.getObject("nr_seq_schedule");
        Long doctorId = (Long) rs.getObject("nr_seq_doctor");
        Timestamp eventTs = rs.getTimestamp("schedule_date");
        Timestamp createdTs = rs.getTimestamp("created_at");

        hr.setId(id);
        hr.setPatientId(patientId);
        hr.setScheduleId(scheduleId);
        hr.setDoctorId(doctorId);
        hr.setEventType(rs.getString("status"));
        hr.setEventTime(eventTs != null ? eventTs.toLocalDateTime() : null);
        hr.setPayload(rs.getString("payload"));
        hr.setCreatedAt(createdTs != null ? createdTs.toInstant() : null);
        return hr;
    }
}
