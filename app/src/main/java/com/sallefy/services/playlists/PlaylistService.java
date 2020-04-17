package com.sallefy.services.playlists;

import com.sallefy.model.Playlist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlaylistService {

    @GET("me/playlists")
    Call<List<Playlist>> getMyPlaylists();
}
