package com.care.hub.data.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDate data;
    private LocalTime hora;
    private String observation;
    private String status;
}
