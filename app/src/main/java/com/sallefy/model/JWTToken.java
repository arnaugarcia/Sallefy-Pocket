package com.sallefy.model;

import com.google.gson.annotations.SerializedName;

public class JWTToken {

    @SerializedName("id_token")
    private final String token;

    public JWTToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
