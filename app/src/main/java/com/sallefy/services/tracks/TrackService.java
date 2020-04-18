package com.sallefy.services.tracks;

import com.sallefy.model.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TrackService {

    @GET("me/tracks")
    Call<List<Track>> getMyTracks(@Header("Authorization") String userToken);
}
