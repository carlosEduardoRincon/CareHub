package com.care.hub.config;

import com.care.hub.exceptions.HistoryRecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class HistoryExceptionHandler {

    @ExceptionHandler(HistoryRecordNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleHistoryRecordNotFound(HistoryRecordNotFoundException ex,
                                                                           WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("path", request != null ? request.getDescription(false) : null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
