package com.care.hub.data.entities;

public class Nurse {
    private Long id;
    private String name;
    private String coren;
    private Long userId;

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

    public String getCoren() {
        return coren;
    }

    public Nurse setCoren(String coren) {
        this.coren = coren;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
