package com.sallefy.managers.playlists;

import android.util.Log;

import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.BaseManager;
import com.sallefy.model.Playlist;
import com.sallefy.model.UserToken;
import com.sallefy.services.authentication.TokenStoreManager;
import com.sallefy.services.playlists.PlaylistService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaylistManager extends BaseManager {

    private static PlaylistManager instance;
    private Retrofit retrofit;
    private PlaylistService playlistService;

    private PlaylistManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        playlistService = retrofit.create(PlaylistService.class);
    }

    public static PlaylistManager getInstance() {
        if (instance == null) instance = new PlaylistManager();
        return instance;
    }

    public synchronized void getMyPlaylists(final PlaylistCallback playlistCallback) {
        String userToken = TokenStoreManager.getInstance().getToken();

        Call<List<Playlist>> call = playlistService.getMyPlaylists("Bearer " + userToken);
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    playlistCallback.onMyPlaylistsReceived(response.body());
                } else {
                    Log.d("ncs", "Error MyPlaylists not successful: " + response.toString());

                    try {
                        playlistCallback.onMyPlaylistsFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d("ncs", "Error MyPlaylists not successful: " + Arrays.toString(t.getStackTrace()));
                playlistCallback.onMyPlaylistsFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }
}
