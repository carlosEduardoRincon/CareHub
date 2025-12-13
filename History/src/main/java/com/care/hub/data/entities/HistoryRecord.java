package com.care.hub.data.entities;

import java.time.Instant;
import java.time.LocalDateTime;

public class HistoryRecord {

    private Long id;

    private Long patientId;

    private Long scheduleId;

    private Long doctorId;

    private String eventType;

    private LocalDateTime eventTime;

    private String payload;

    private Instant createdAt;

    public HistoryRecord() {
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}
