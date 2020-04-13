package com.sallefy.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sallefy.constants.ApplicationConstants;
import com.sallefy.services.authentication.TokenStoreManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseManager {
    protected Retrofit retrofit;

    public BaseManager() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.connectTimeout(500, TimeUnit.SECONDS)
                .readTimeout(500, TimeUnit.SECONDS);

        httpClient.addInterceptor(chain -> {
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
        });

        OkHttpClient client = httpClient.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

    }
}
