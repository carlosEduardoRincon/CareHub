package com.care.hub.dtos;

public class EditHistoryRecordInput {
    private Long id;
    private String eventType;
    private String eventTime; 
    private Long scheduleId;

    public EditHistoryRecordInput(String eventType, String eventTime, Long scheduleId) {
        this.eventType = eventType;
        this.eventTime = eventTime;
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

    public Long getScheduleId() {
        return scheduleId;
    }
}
