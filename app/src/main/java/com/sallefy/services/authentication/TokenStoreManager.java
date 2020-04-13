package com.sallefy.services.authentication;

public class TokenStoreManager {
    private static TokenStoreManager instance;
    private String token;

    public static TokenStoreManager getInstance() {
        if(instance == null){
            instance = new TokenStoreManager();
        }
        return instance;
    }

    private TokenStoreManager(){
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}


