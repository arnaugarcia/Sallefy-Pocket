package com.sallefy.services.genres;

import com.sallefy.model.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GenreService {

    @GET("genres")
    Call<List<Genre>> getAllGenres(@Header("Authorization") String userToken);
}
