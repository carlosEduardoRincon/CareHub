package com.care.hub.scheduling.entities;

import java.time.LocalDateTime;

public class Schedule {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Patient patient;
    private Doctor doctor;
}
