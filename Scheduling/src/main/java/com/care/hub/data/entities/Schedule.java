
package com.care.hub.data.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Schedule {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDateTime scheduleDate;
    private String observation;
    private String status;

    public Long getId() {
        return id;
    }

    public Schedule setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public Schedule setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Schedule setPatientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }

    public LocalDateTime getScheduleDate() {
        return scheduleDate;
    }

    public Schedule setScheduleDate(LocalDateTime scheduleDate) {
        this.scheduleDate = scheduleDate;
        return this;
    }

    public String getObservation() {
        return observation;
    }

    public Schedule setObservation(String observation) {
        this.observation = observation;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Schedule setStatus(String status) {
        this.status = status;
        return this;
    }
}
