package com.care.hub.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "history_records")
public class HistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientId;

    @Column
    private String scheduleId;

    @Column
    private String doctorId;

    @Column
    private String eventType;

    @Column
    private Instant eventTime;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private Instant createdAt;

    public HistoryRecord() {
    }

    public Long getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
