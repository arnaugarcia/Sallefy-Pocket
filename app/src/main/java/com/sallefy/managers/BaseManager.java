package com.sallefy.managers;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sallefy.activity.LoginActivity;
import com.sallefy.constants.ApplicationConstants;
import com.sallefy.services.authentication.TokenStoreManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.core.content.ContextCompat.startActivity;
import static java.net.HttpURLConnection.*;

public abstract class BaseManager {
    protected Retrofit retrofit;

    public BaseManager() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.connectTimeout(500, TimeUnit.SECONDS)
                .readTimeout(500, TimeUnit.SECONDS);

        httpClient.addInterceptor(tokenInterceptor());

        httpClient.addInterceptor(forbiddenInterceptor());

        OkHttpClient client = httpClient.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

    }

    private Interceptor tokenInterceptor() {
        return chain -> {
            Request original = chain.request();

            String bearerToken = TokenStoreManager.getInstance().getToken();

            if (bearerToken == null || bearerToken.equals("")) {
                throw new RuntimeException("Token is missing"); // Transform this to a BusinessException
            }

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", TokenStoreManager.getInstance().getToken() + " " + bearerToken); // <-- this is the important line

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    private Interceptor forbiddenInterceptor() {
        return chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if (response.code() == HTTP_FORBIDDEN) {
                        goToLogin();
                    }
                    return response;
                };
    }

    private void goToLogin() {
        Intent intent = new Intent(TokenStoreManager.getInstance().getContext(), LoginActivity.class);
        TokenStoreManager.getInstance().getContext().startActivity(intent);
    }
}
