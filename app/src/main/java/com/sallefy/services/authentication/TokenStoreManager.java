package com.sallefy.services.authentication;

import android.content.Context;

public class TokenStoreManager {
    private static TokenStoreManager instance;
    private Context context;
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}


