package com.care.hub.data.entities;

import java.util.List;

public class User {
    private Long id;
    private String username;
    private String password;
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }
}
