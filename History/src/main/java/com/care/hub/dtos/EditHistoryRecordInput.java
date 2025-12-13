package com.care.hub.dtos;

public class EditHistoryRecordInput {
    private Long id;
    private String eventType;
    private String eventTime; 
    private String payload;
    private Long scheduleId;

    public EditHistoryRecordInput(String eventType, String eventTime, String payload, Long scheduleId) {
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.payload = payload;
        this.scheduleId = scheduleId;
    }

    public Long getId() {
        return id;
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

    public Long getScheduleId() {
        return scheduleId;
    }
}
