package com.sallefy.services.authentication;

import com.sallefy.model.JWTToken;
import com.sallefy.model.UserCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationService {

    @POST("authenticate")
    Call<JWTToken> authenticate(@Body UserCredentials userCredentials);
}
