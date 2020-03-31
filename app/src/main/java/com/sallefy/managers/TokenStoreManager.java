package com.sallefy.managers;

public class TokenStoreManager {
    private static TokenStoreManager instance;
    private String token;
    private String login;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}

