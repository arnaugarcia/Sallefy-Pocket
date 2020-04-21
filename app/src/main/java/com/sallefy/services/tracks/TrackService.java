package com.sallefy.services.tracks;

import com.sallefy.model.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TrackService {

    @GET("me/tracks")
    Call<List<Track>> getMyTracks(@Header("Authorization") String userToken);

    @GET("tracks")
    Call<List<Track>> getTracksByGenre(@Header("Authorization") String userToken, @Query("genre") String genre);
}
