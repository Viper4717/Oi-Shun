package com.example.oishun;

public class User {
    private String name;
    private String passwrod;

    public User() {
    }

    public User(String name, String passwrod) {
        this.name = name;
        this.passwrod = passwrod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswrod() {
        return passwrod;
    }

    public void setPasswrod(String passwrod) {
        this.passwrod = passwrod;
    }
}
