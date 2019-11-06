package com.example.oishun;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String password;
    private String userAvatarURL;
   // private ArrayList<String> subscribedUserNames;

    public User() {
    }

    public User(String name, String email, String userAvatarURL) {
        this.name = name;
        this.email = email;
        this.userAvatarURL = userAvatarURL;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserAvatarURL() {
        return userAvatarURL;
    }

    public void setUserAvatarURL(String userAvatarURL) {
        this.userAvatarURL = userAvatarURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
