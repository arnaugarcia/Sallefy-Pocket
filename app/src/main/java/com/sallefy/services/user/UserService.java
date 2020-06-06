package com.sallefy.services.user;

import com.sallefy.model.Playlist;
import com.sallefy.model.Track;
import com.sallefy.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("account")
    Call<User> getUserData();

    @GET("users")
    Call<List<User>> getMostFollowedUsers(@Query("sort") String sortByFollowers);

    @GET("users/{login}/tracks")
    Call<List<Track>> getTracksByLogin(@Path("login") String login);

    @GET("users/{login}/playlists")
    Call<List<Playlist>> getPlaylistsByLogin(@Path("login") String login);
}
