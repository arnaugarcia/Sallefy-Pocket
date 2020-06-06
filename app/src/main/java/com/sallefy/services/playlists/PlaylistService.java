package com.sallefy.services.playlists;

import com.sallefy.model.LikedDTO;
import com.sallefy.model.Playlist;
import com.sallefy.model.PlaylistRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlaylistService {

    @GET("me/playlists")
    Call<List<Playlist>> getMyPlaylists();

    @POST("playlists")
    Call<Playlist> createPlaylist(@Body PlaylistRequest playlistRequest);

    @GET("playlists")
    Call<List<Playlist>> getMostFollowedPlaylists(@Query("sort") String sortByFollowers);

    @PUT("playlists")
    Call<Playlist> updatePlaylist(@Body PlaylistRequest playlistRequest);

    @PUT("playlists/{id}/follow")
    Call<LikedDTO> followPlaylist(@Path("id") String id);

    @GET("me/playlists/following")
    Call<List<Playlist>> getMyFollowedPlaylists();

}
