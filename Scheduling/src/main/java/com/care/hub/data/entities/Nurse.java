
package com.care.hub.data.entities;

public class Nurse {
    private Long id;
    private String name;
    private String login;
    private String password;
    private String cpf;
    private String coren;

    public Long getId() {
        return id;
    }

    public Nurse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Nurse setName(String name) {
        this.name = name;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Nurse setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Nurse setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getCpf() {
        return cpf;
    }

    public Nurse setCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public String getCoren() {
        return coren;
    }

    public Nurse setCoren(String coren) {
        this.coren = coren;
        return this;
    }
}
