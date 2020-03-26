package com.sallefy.model;

import com.google.gson.annotations.SerializedName;

public class UserToken {

    @SerializedName("id_token")
    private String idToken;

    public UserToken(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
