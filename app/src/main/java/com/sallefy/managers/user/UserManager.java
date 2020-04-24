package com.sallefy.managers.user;

import android.content.Context;
import android.util.Log;

import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.BaseManager;
import com.sallefy.model.User;
import com.sallefy.services.authentication.AuthenticationUtils;
import com.sallefy.services.user.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserManager extends BaseManager {

    private static UserManager instance;
    private Retrofit retrofit;
    private UserService userService;

    public UserManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public synchronized void getUserData(Context context, final UserDataCallback userDataCallback) {
        String userToken = AuthenticationUtils.getToken(context);

        Call<User> call = userService.getUserData("Bearer " + userToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    userDataCallback.onUserDataSuccess(response.body());
                } else {
                    Log.d("ncs", "Error UserData not successful: " + response.toString());

                    try {
                        userDataCallback.onUserDataFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("ncs", "Error UserData failure: " + Arrays.toString(t.getStackTrace()));
                userDataCallback.onUserDataFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }

    public synchronized void getPopularUsers(Context context, final PopularUsersCallback popularUsersCallback) {
        String userToken = AuthenticationUtils.getToken(context);

        Call<List<User>> call = userService.getPopularUsers("Bearer " + userToken, true, "followers,desc");
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    popularUsersCallback.onPopularUsersSuccess(response.body());
                } else {
                    Log.d("ncs", "Error PopularUsers not successful: " + response.toString());

                    try {
                        popularUsersCallback.onPopularUsersFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("ncs", "Error PopularUsers failure: " + Arrays.toString(t.getStackTrace()));
                popularUsersCallback.onPopularUsersFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }
}
