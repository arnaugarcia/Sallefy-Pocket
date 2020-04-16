package com.sallefy.managers.registration;

import android.util.Log;

import com.sallefy.constants.ApplicationConstants;
import com.sallefy.model.UserRegister;
import com.sallefy.services.registration.RegistrationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationManager {

    private static RegistrationManager instance;
    private Retrofit retrofit;
    private RegistrationService registrationService;

    private RegistrationManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        registrationService = retrofit.create(RegistrationService.class);
    }

    public static RegistrationManager getInstance() {
        if (instance == null) instance = new RegistrationManager();
        return instance;
    }

    public synchronized void register(UserRegister userRegister, final RegistrationCallback registrationCallback) {
        Call<ResponseBody> call = registrationService.registerUser(userRegister);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();

                if (code == 200 || code == 201) {
                    if (response.body() != null) {
                        registrationCallback.onRegistrationSuccess();
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        registrationCallback.onRegistrationFailure(new Throwable(error.get("detail").toString()));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(call.toString());
                Log.e(this.getClass().getName(), "Error in registration: " + t);

                if (t instanceof RuntimeException) {
                    Log.d("nfc => T", t.getMessage());
                }
                registrationCallback.onRegistrationFailure(t);
            }
        });
    }
}
