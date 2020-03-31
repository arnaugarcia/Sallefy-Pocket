package com.sallefy.managers.authentication;

import android.util.Log;

import com.sallefy.managers.BaseManager;
import com.sallefy.model.JWTToken;
import com.sallefy.services.authentication.AuthenticationService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationManager extends BaseManager {
    private static AuthenticationManager instance;
    private AuthenticationService authService;
    private String token;

    private AuthenticationManager() {
        authService = retrofit.create(AuthenticationService.class);
    }

    public static AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }

        return instance;
    }

    public synchronized void authenticate(String login, String password, final AuthenticationCallback userDetailCallback) {
        Call<JWTToken> call = authService.authenticate(login, password);

        call.enqueue(new Callback<JWTToken>() {
            @Override
            public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {

                int code = response.code();

                if (code == 200 || code == 201) {
                    if (token != null) {
                        userDetailCallback.onAuthenticationSuccess(response.body());
                    }
                } else {
                    userDetailCallback.onAuthenticationFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<JWTToken> call, Throwable t) {
                Log.e("StudyManager->", "getAllStudies()->ERROR: " + t);


                if (t instanceof RuntimeException) {
                    Log.d("nxw => T", t.getMessage());
                }
                userDetailCallback.onAuthenticationFailure(t);
            }
        });
    }
}
