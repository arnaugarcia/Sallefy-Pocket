package com.sallefy.managers.tracks;

import android.content.Context;
import android.util.Log;

import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.BaseManager;
import com.sallefy.model.Track;
import com.sallefy.services.authentication.AuthenticationUtils;
import com.sallefy.services.tracks.TrackService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public synchronized void getMyTracks(Context context, final TrackCallback trackCallback) {
        String userToken = AuthenticationUtils.getToken(context);

        Call<List<Track>> call = trackService.getMyTracks("Bearer " + userToken);
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    trackCallback.onMyTracksReceived(response.body());
                } else {
                    Log.d("ncs", "Error MyTracks not successful: " + response.toString());

                    try {
                        trackCallback.onMyTracksFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d("ncs", "Error MyTracks not successful: " + Arrays.toString(t.getStackTrace()));
                trackCallback.onMyTracksFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }
}
