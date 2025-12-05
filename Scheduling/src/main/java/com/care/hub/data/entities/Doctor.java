package com.care.hub.data.entities;

public class Doctor {
    private Long id;
    private String crm;
    private String speciality;
    private Long userId;

    public String getCrm() {
        return crm;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Long getUserId() {
        return userId;
    }
}
