package com.sallefy.model;

import com.google.gson.annotations.SerializedName;

public class UserCredentials {

    @SerializedName("username")
    private final String login;

    @SerializedName("password")
    private final String password;

    @SerializedName("rememberMe")
    private final boolean rememberMe;

    public UserCredentials(String login, String password, boolean rememberMe) {
        this.login = login;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }
}
