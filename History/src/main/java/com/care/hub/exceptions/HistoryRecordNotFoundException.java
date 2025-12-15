package com.care.hub.exceptions;

public class HistoryRecordNotFoundException extends RuntimeException {
    public HistoryRecordNotFoundException(Long historyRecordId) {
        super("History record not found with historyRecordId: " + historyRecordId);
    }
}
