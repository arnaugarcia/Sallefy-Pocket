package com.sallefy.managers.tracks;

import android.content.Context;
import android.util.Log;

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

    private Retrofit retrofit;
    private TrackService trackService;

    private TrackManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        trackService = retrofit.create(TrackService.class);
    }

    public static TrackManager getInstance() {
        if (instance == null) instance = new TrackManager();
        return instance;
    }

    public synchronized void getMyTracks(Context context, final MyTracksCallback myTracksCallback) {
        String userToken = AuthenticationUtils.getToken(context);

        Call<List<Track>> call = trackService.getMyTracks("Bearer " + userToken);
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

    public synchronized void getTracksByGenre(Context context, String genre, final TracksByGenreCallback tracksByGenreCallback) {
        String userToken = AuthenticationUtils.getToken(context);

        Call<List<Track>> call = trackService.getTracksByGenre("Bearer " + userToken, genre);
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

    public synchronized void updateTrackLiked(Context context, String id, final UpdateTrackLikedCallback callback){
        String userToken = AuthenticationUtils.getToken(context);

        Call<LikedDTO> call = trackService.updateTrackLiked("Bearer " + userToken, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    callback.onMyTracksSuccess(response.body());
                } else {
                    Log.d(ApplicationConstants.LOGCAT_ID, "Error  like not successful: " + response.toString());
                    callback.onMyTracksFailure(new Throwable("Error " + code + ", " + response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(ApplicationConstants.LOGCAT_ID, "Error  like not successful: " + Arrays.toString(t.getStackTrace()));
                callback.onMyTracksFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));

            }
        });
    }
}
