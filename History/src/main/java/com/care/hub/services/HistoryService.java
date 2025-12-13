package com.care.hub.services;

import com.care.hub.data.entities.HistoryRecord;
import com.care.hub.data.repositories.HistoryRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistoryService {

    private final HistoryRecordRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HistoryService(HistoryRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveFromMessage(Object message) {
        Map<String, Object> map = coerceToMap(message);
        Map<String, Object> schedule;
        if (map.containsKey("schedule")) {
            schedule = coerceToMap(map.get("schedule"));
        } else {
            schedule = map;
        }

        var record = new HistoryRecord();

        var patientId = schedule.get("nr_seq_patient");
        record.setPatientId(patientId == null ? null : ((Integer) patientId).longValue());

        var scheduleId = schedule.get("nr_seq_schedule");
        record.setScheduleId(scheduleId == null ? null : ((Integer) scheduleId).longValue());

        var doctorId = schedule.get("nr_seq_doctor");
        record.setDoctorId(doctorId == null ? null : ((Integer) doctorId).longValue());

        var eventType = map.get("eventType");
        record.setEventType(eventType == null ? null : String.valueOf(eventType));

        Instant eventTime = null;
        var scheduleDate = schedule.get("schedule_date");
        if (scheduleDate instanceof java.util.List<?> list && list.size() >= 5) {
            int year = ((Number) list.get(0)).intValue();
            int month = ((Number) list.get(1)).intValue();
            int day = ((Number) list.get(2)).intValue();
            int hour = ((Number) list.get(3)).intValue();
            int minute = ((Number) list.get(4)).intValue();
            eventTime = java.time.LocalDateTime.of(year, month, day, hour, minute)
                    .toInstant(java.time.ZoneOffset.UTC);
        } else if (scheduleDate != null) {
            eventTime = parseInstantSafe(String.valueOf(scheduleDate));
        }

        record.setEventTime(eventTime);
        record.setPayload(writeJsonSafe(message));
        record.setCreatedAt(Instant.now());

        if (record.getPatientId() == null) {
            throw new IllegalArgumentException("patientId é obrigatório no evento.");
        }

        repository.save(record);
    }

    private Map<String, Object> coerceToMap(Object message) {
        if (message instanceof Map<?, ?> map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> cast = (Map<String, Object>) map;
            return cast;
        }
        return objectMapper.convertValue(message, Map.class);
    }

    private Instant parseInstantSafe(String s) {
        try {
            return Instant.parse(s);
        } catch (DateTimeParseException e) {
            try {
                return OffsetDateTime.parse(s).toInstant();
            } catch (DateTimeParseException ignored) {
                return null;
            }
        }
    }

    private String writeJsonSafe(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return String.valueOf(obj);
        }
    }
}