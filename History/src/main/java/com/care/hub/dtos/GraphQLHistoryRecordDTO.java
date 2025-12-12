package com.care.hub.dtos;

public class GraphQLHistoryRecordDTO {

    private final String id;
    private final String patientId;
    private final String appointmentId;
    private final String eventType;
    private final String eventTime;
    private final String payload;
    private final String createdAt;

    public GraphQLHistoryRecordDTO(String id, String patientId, String appointmentId, String eventType, String eventTime, String payload, String createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getPayload() {
        return payload;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
