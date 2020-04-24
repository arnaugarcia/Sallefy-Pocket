package com.sallefy.services.user;

import com.sallefy.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface UserService {

    @GET("account")
    Call<User> getUserData(@Header("Authorization") String userToken);

    @GET("users")
    Call<List<User>> getPopularUsers(@Header("Authorization") String userToken,
                                     @Query("popular") boolean popular,
                                     @Query("sort") String sortByFollowers);
}
