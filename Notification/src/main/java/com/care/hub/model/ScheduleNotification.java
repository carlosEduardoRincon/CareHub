package com.care.hub.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ScheduleNotification {
    private Long scheduleId;
    private LocalDateTime date;
    private String observation;
    private String patientEmail;
    private String patientName;
    private String doctorEmail;
    private String doctorName;

    public Long getScheduleId() {
        return scheduleId;
    }

    public ScheduleNotification setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
        return this;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ScheduleNotification setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public String getObservation() {
        return observation;
    }

    public ScheduleNotification setObservation(String observation) {
        this.observation = observation;
        return this;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public ScheduleNotification setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
        return this;
    }

    public String getPatientName() {
        return patientName;
    }

    public ScheduleNotification setPatientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public ScheduleNotification setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
        return this;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public ScheduleNotification setDoctorName(String doctorName) {
        this.doctorName = doctorName;
        return this;
    }
}
