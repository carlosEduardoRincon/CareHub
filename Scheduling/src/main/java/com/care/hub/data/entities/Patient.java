package com.care.hub.data.entities;

import java.time.LocalDate;

public class Patient {
    private Long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private String address;
    private String telephone;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public Long getUserId() {
        return userId;
    }

    public Patient setId(Long id) {
        this.id = id;
        return this;
    }

    public Patient setName(String name) {
        this.name = name;
        return this;
    }

    public Patient setEmail(String email) {
        this.email = email;
        return this;
    }

    public Patient setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public Patient setAddress(String address) {
        this.address = address;
        return this;
    }

    public Patient setTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public Patient setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
