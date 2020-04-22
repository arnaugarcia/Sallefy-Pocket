package com.sallefy.services.user;

import com.sallefy.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {

    @GET("account")
    Call<User> getUserData(@Header("Authorization") String userToken);
}
