package com.sallefy.model;

public class JWTToken {
    private final String token;

    public JWTToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
