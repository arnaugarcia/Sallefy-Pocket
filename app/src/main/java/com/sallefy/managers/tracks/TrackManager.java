package com.sallefy.managers.tracks;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.BaseManager;
import com.sallefy.model.LikedDTO;
import com.sallefy.model.Track;
import com.sallefy.services.authentication.AuthenticationUtils;
import com.sallefy.services.tracks.TrackService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackManager extends BaseManager {

    private static TrackManager instance;
    private TrackService trackService;

    private TrackManager() {
        trackService = retrofit.create(TrackService.class);
    }

    public static TrackManager getInstance() {
        if (instance == null) instance = new TrackManager();
        return instance;
    }

    public synchronized void getMyTracks(final MyTracksCallback myTracksCallback) {

        Call<List<Track>> call = trackService.getMyTracks();
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    myTracksCallback.onMyTracksReceived(response.body());
                } else {
                    Log.d(ApplicationConstants.LOGCAT_ID, "Error MyTracks not successful: " + response.toString());

                    try {
                        myTracksCallback.onMyTracksFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(ApplicationConstants.LOGCAT_ID, "Error MyTracks not successful: " + Arrays.toString(t.getStackTrace()));
                myTracksCallback.onMyTracksFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }

    public synchronized void getTracksByGenre(String genre, final TracksByGenreCallback tracksByGenreCallback) {

        Call<List<Track>> call = trackService.getTracksByGenre(genre);
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    tracksByGenreCallback.onTracksByGenreReceived(response.body());
                } else {
                    Log.d(ApplicationConstants.LOGCAT_ID, "Error TracksByGenre not successful: " + response.toString());

                    try {
                        tracksByGenreCallback.onTracksByGenreFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(ApplicationConstants.LOGCAT_ID, "Error TracksByGenre not successful: " + Arrays.toString(t.getStackTrace()));
                tracksByGenreCallback.onTracksByGenreFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }

    public synchronized void getMostPlayedTracks(final MostPlayedTracksCallback mostPlayedTracksCallback) {

        Call<List<Track>> call = trackService.getMostPlayedTracks(true);
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    mostPlayedTracksCallback.onMostPlayedTracksSuccess(response.body());
                } else {
                    Log.d("ncs", "Error PopularTracks not successful: " + response.toString());

                    try {
                        mostPlayedTracksCallback.onMostPlayedTracksFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d("ncs", "Error PopularTracks failure: " + Arrays.toString(t.getStackTrace()));
                mostPlayedTracksCallback.onMostPlayedTracksFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }

    public synchronized void updateTrackLiked(String id, final UpdateTrackLikedCallback callback){

        Call<LikedDTO> call = trackService.updateTrackLiked(id);
        call.enqueue(new Callback<LikedDTO>() {
            @Override
            public void onResponse(Call<LikedDTO> call, Response<LikedDTO> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    callback.onMyTracksSuccess(response.body());
                } else {
                    Log.d(ApplicationConstants.LOGCAT_ID, "Error  like not successful: " + response.toString());
                    callback.onMyTracksFailure(new Throwable("Error " + code + ", " + response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<LikedDTO> call, Throwable t) {
                Log.d(ApplicationConstants.LOGCAT_ID, "Error  like not successful: " + Arrays.toString(t.getStackTrace()));
                callback.onMyTracksFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));

            }
        });
    }

    public synchronized void getTrack(String id, final TrackCallback trackCallback) {

        Call<Track> call = trackService.findTrack(id);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                trackCallback.onTrackSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Track> call, Throwable throwable) {
                trackCallback.onTrackFailure(throwable);
            }
        });
    }
}
