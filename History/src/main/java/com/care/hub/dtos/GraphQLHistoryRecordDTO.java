package com.care.hub.dtos;

public record GraphQLHistoryRecordDTO(Long historyId, Long patientId, Long scheduleId, Long doctorId, String eventType,
                                      String eventTime, String payload, String createdAt) {
}
