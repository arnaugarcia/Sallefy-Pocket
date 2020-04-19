package com.sallefy.managers.genres;

import android.content.Context;
import android.util.Log;

import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.BaseManager;
import com.sallefy.model.Genre;
import com.sallefy.services.authentication.AuthenticationUtils;
import com.sallefy.services.genres.GenreService;

import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreManager extends BaseManager {
    private static GenreManager instance;
    private Retrofit retrofit;
    private GenreService genreService;

    public GenreManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        genreService = retrofit.create(GenreService.class);
    }

    public static GenreManager getInstance(){
        if (instance == null) instance = new GenreManager();
        return instance;
    }

    public synchronized void getAllGenres(Context context, final GenresCallback genresCallback){
        String userToken = AuthenticationUtils.getToken(context);

        Call<List<Genre>> call = genreService.getAllGenres("Bearer " + userToken);
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                int code = response.code();

                if(response.isSuccessful()){
                    genresCallback.onGetAllGenresReceived(response.body());
                }else {
                    Log.d(ApplicationConstants.LOGCAT_ID, "Error GetAllGenres not successful: " + response.toString());
                    try{
                        genresCallback.onGetAllGenresFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.d(ApplicationConstants.LOGCAT_ID, "Error GetAllGenres not successful: " + Arrays.toString(t.getStackTrace()));
                genresCallback.onGetAllGenresFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }
}
