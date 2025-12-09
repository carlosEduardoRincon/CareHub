
package com.care.hub.data.entities;

public class Doctor {
    private Long id;
    private String name;
    private String email;
    private String crm;
    private String speciality;
    private Long userId;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCrm() {
        return crm;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Long getUserId() {
        return userId;
    }

    public Doctor setId(Long id) {
        this.id = id;
        return this;
    }

    public Doctor setName(String name) {
        this.name = name;
        return this;
    }

    public Doctor setEmail(String email) {
        this.email = email;
        return this;
    }

    public Doctor setSpeciality(String speciality) {
        this.speciality = speciality;
        return this;
    }

    public Doctor setCrm(String crm) {
        this.crm = crm;
        return this;
    }

    public Doctor setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}