
package com.care.hub.data.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDate scheduleDate;
    private LocalTime scheduleHour;
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

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public Schedule setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
        return this;
    }

    public LocalTime getScheduleHour() {
        return scheduleHour;
    }

    public Schedule setScheduleHour(LocalTime scheduleHour) {
        this.scheduleHour = scheduleHour;
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
