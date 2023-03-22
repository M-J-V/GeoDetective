package com.example.geodetective;

public class ActiveUser {
    private String username;
    private String password;
    private boolean trusted;

    private static ActiveUser User = null;

    private ActiveUser() {
        this.username = null;
        this.password = null;
        this.trusted = false;
    }

    public static ActiveUser getInstance() {
        if (User == null) {
            User = new ActiveUser();
        }
        return User;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getTrusted() {
        return trusted;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTrusted(boolean trust) {
        this.trusted = trust;
    }

    public void disconnectUser() {
        this.User = null;
        this.username = "";
        this.password = "";
        this.trusted = false;
    }

}
