package com.sallefy.services.playlists;

import com.sallefy.model.Playlist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PlaylistService {

    @GET("me/playlists")
    Call<List<Playlist>> getMyPlaylists(@Header("Authorization") String userToken);
}
