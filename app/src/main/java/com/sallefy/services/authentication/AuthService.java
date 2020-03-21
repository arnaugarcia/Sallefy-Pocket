package com.sallefy.services.authentication;

import retrofit2.Call;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/authenticate")
    Call<Object> basicLogin();
}
