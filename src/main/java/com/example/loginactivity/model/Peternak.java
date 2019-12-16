package com.example.loginactivity.model;

public class Peternak {
    private String username;
    private String email;

    public Peternak() {
    }

    public Peternak(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
