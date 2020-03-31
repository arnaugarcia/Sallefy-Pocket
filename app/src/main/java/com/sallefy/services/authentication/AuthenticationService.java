package com.sallefy.services.authentication;

import com.sallefy.model.JWTToken;

import retrofit2.Call;
import retrofit2.http.POST;

public interface AuthenticationService {

    @POST("/authenticate")
    Call<JWTToken> authenticate(String login, String password);
}
