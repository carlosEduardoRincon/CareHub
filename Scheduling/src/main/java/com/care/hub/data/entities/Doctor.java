
package com.care.hub.data.entities;

public class Doctor {
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private String cpf;
    private String crm;
    private String speciality;

    public Long getId() {
        return id;
    }

    public Doctor setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Doctor setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Doctor setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Doctor setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Doctor setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getCpf() {
        return cpf;
    }

    public Doctor setCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public String getCrm() {
        return crm;
    }

    public Doctor setCrm(String crm) {
        this.crm = crm;
        return this;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Doctor setSpeciality(String speciality) {
        this.speciality = speciality;
        return this;
    }
}