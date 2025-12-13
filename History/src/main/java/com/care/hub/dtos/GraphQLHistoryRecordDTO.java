package com.care.hub.dtos;

public class GraphQLHistoryRecordDTO {

    private final Long id;
    private final Long patientId;
    private final Long scheduleId;
    private final Long doctorId;
    private final String eventType;
    private final String eventTime;
    private final String payload;
    private final String createdAt;

    public GraphQLHistoryRecordDTO(Long id,
                                   Long patientId,
                                   Long scheduleId,
                                   Long doctorId,
                                   String eventType,
                                   String eventTime,
                                   String payload,
                                   String createdAt
    ) {
        this.id = id;
        this.patientId = patientId;
        this.scheduleId = scheduleId;
        this.doctorId = doctorId;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.payload = payload;
        this.createdAt = createdAt;
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
