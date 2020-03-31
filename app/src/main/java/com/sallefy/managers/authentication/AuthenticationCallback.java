package com.sallefy.managers.authentication;

import com.sallefy.model.JWTToken;

public interface AuthenticationCallback {
    void onSuccess(JWTToken token);
    void onFailure(Throwable throwable);
}
