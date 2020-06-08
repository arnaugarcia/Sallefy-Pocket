package com.sallefy.services.tracks;

import com.sallefy.model.LikedDTO;
import com.sallefy.model.Track;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrackService {

    @GET("me/tracks")
    Call<List<Track>> getMyTracks();

    @GET("tracks")
    Call<List<Track>> getTracksByGenre(@Query("genre") String genre);

    @PUT("tracks/{id}/like")
    Call<LikedDTO> updateTrackLiked(@Path("id") String id);

    // TODO: Most liked tracks backend bug
    @GET("tracks")
    Call<List<Track>> getMostPlayedTracks(@Query("played") boolean sortByPlays);

    @GET("tracks/{id}")
    Call<Track> findTrack(@Path("id") String id);
}
