package com.zenrabanes.chat_app.Model;

public class User {

    private String id;
    private String email;

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
