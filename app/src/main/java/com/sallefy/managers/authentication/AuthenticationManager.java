package com.sallefy.managers.authentication;

import android.util.Log;

import com.sallefy.model.JWTToken;
import com.sallefy.model.UserCredentials;
import com.sallefy.services.authentication.AuthenticationService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.sallefy.constants.ApplicationConstants.API_BASE_URL;
import static retrofit2.converter.gson.GsonConverterFactory.create;

public class AuthenticationManager {
    private static AuthenticationManager instance;
    private Retrofit retrofit;
    private AuthenticationService authService;

    private AuthenticationManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(create())
                .build();
        authService = retrofit.create(AuthenticationService.class);
    }

    public static AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }

        return instance;
    }

    public synchronized void authenticate(UserCredentials userCredentials, final AuthenticationCallback userDetailCallback) {
        Call<JWTToken> call = authService.authenticate(userCredentials);

        call.enqueue(new Callback<JWTToken>() {
            @Override
            public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {
                int code = response.code();

                if (code == 200 || code == 201) {
                    if (response.body() != null) {
                        userDetailCallback.onAuthenticationSuccess(response.body());
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        userDetailCallback.onAuthenticationFailure(new Throwable(error.get("detail").toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JWTToken> call, Throwable t) {
                System.out.println(call.toString());
                Log.e(this.getClass().getName(), "Error authenticating " + t);

                if (t instanceof RuntimeException) {
                    Log.d("nxw => T", t.getMessage());
                }
                userDetailCallback.onAuthenticationFailure(t);
            }
        });
    }
}
