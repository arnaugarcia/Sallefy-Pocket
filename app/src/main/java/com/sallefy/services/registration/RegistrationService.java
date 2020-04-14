package com.sallefy.services.registration;

import com.sallefy.model.UserRegister;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistrationService {

    @POST("register")
    Call<ResponseBody> registerUser(@Body UserRegister user);
}
