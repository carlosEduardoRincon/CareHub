package com.care.hub.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EditHistoryRecordInput {
    private Long historyId;
    private String eventType;
    private String eventTime;
    private Long scheduleId;

    public EditHistoryRecordInput() {}

    @JsonCreator
    public EditHistoryRecordInput(
            @JsonProperty("historyId") Long historyId,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("eventTime") String eventTime,
            @JsonProperty("scheduleId") Long scheduleId) {
        this.historyId = historyId;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.scheduleId = scheduleId;
    }

    public Long getHistoryId() {
        return historyId;
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

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}