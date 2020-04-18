package com.sallefy.services.playlists;

import com.sallefy.model.Playlist;
import com.sallefy.model.PlaylistRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PlaylistService {

    @GET("me/playlists")
    Call<List<Playlist>> getMyPlaylists(@Header("Authorization") String userToken);

    @POST("playlists")
    Call<Playlist> createPlaylist(@Header("Authorization") String userToken, @Body PlaylistRequest playlistRequest);
}
