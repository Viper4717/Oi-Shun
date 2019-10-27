package com.example.oishun;

public class User {
    private String name;
    private String password;
    private String userAvatarURL;

    public User() {
    }

    public User(String name, String password, String userAvatarURL) {
        this.name = name;
        this.password = password;
        this.userAvatarURL = userAvatarURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAvatarURL() {
        return userAvatarURL;
    }

    public void setUserAvatarURL(String userAvatarURL) {
        this.userAvatarURL = userAvatarURL;
    }
}
