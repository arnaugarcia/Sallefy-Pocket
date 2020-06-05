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
    Call<List<Playlist>> getMyPlaylists(@Header("Authorization") String userToken);

    @POST("playlists")
    Call<Playlist> createPlaylist(@Header("Authorization") String userToken, @Body PlaylistRequest playlistRequest);

    @GET("playlists")
    Call<List<Playlist>> getMostFollowedPlaylists(@Header("Authorization") String userToken, @Query("sort") String sortByFollowers);

    @PUT("playlists")
    Call<Playlist> updatePlaylist(@Header("Authorization") String userToken, @Body PlaylistRequest playlistRequest);

    @PUT("playlists/{id}/follow")
    Call<LikedDTO> followPlaylist(@Header("Authorization") String userToken, @Path("id") String id);

    @GET("me/playlists/following")
    Call<List<Playlist>> getMyFollowedPlaylists(@Header("Authorization") String userToken);

}
