package com.sallefy.managers.authentication;

import com.sallefy.model.JWTToken;

public interface AuthenticationCallback {
    void onAuthenticationSuccess(JWTToken token);
    void onAuthenticationFailure(Throwable throwable);
}
