package com.sallefy.managers.playlists;

import android.content.Context;
import android.util.Log;

import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.BaseManager;
import com.sallefy.model.LikedDTO;
import com.sallefy.model.Playlist;
import com.sallefy.model.PlaylistRequest;
import com.sallefy.services.authentication.AuthenticationUtils;
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
    private PlaylistService playlistService;

    private PlaylistManager() {
        playlistService = retrofit.create(PlaylistService.class);
    }

    public static PlaylistManager getInstance() {
        if (instance == null) instance = new PlaylistManager();
        return instance;
    }

    public synchronized void getMyPlaylists(final MyPlaylistsCallback myPlaylistsCallback) {

        Call<List<Playlist>> call = playlistService.getMyPlaylists();
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    myPlaylistsCallback.onMyPlaylistsReceived(response.body());
                } else {
                    Log.d("ncs", "Error MyPlaylists not successful: " + response.toString());

                    try {
                        myPlaylistsCallback.onMyPlaylistsFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d("ncs", "Error MyPlaylists failure: " + Arrays.toString(t.getStackTrace()));
                myPlaylistsCallback.onMyPlaylistsFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }

    public synchronized void createPlaylist(PlaylistRequest playlistRequest, final CreatePlaylistCallback createPlaylistCallback) {

        Call<Playlist> call = playlistService.createPlaylist( playlistRequest);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    createPlaylistCallback.onCreatePlaylistSuccess(response.body());
                } else {
                    Log.d("ncs", "Error MyPlaylists not successful: " + response.toString());

                    try {
                        createPlaylistCallback.onCreatePlaylistFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.d("ncs", "Error CreatePlaylist failure: " + Arrays.toString(t.getStackTrace()));
                createPlaylistCallback.onCreatePlaylistFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }

    public synchronized void getMostFollowedPlaylists(final MostFollowedPlaylistsCallback mostFollowedPlaylistsCallback) {

        Call<List<Playlist>> call = playlistService.getMostFollowedPlaylists("followers,desc");
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    mostFollowedPlaylistsCallback.onMostFollowedPlaylistsSuccess(response.body());
                } else {
                    Log.d("ncs", "Error MostFollowedPlaylist not successful: " + response.toString());

                    try {
                        mostFollowedPlaylistsCallback.onMostFollowedPlaylistsFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d("ncs", "Error MostFollowedPlaylist failure: " + Arrays.toString(t.getStackTrace()));
                mostFollowedPlaylistsCallback.onMostFollowedPlaylistsFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });

    }

    public synchronized void updatePlaylist(PlaylistRequest playlistRequest, final UpdatePlaylistCallback callback){

        Call<Playlist> call = playlistService.updatePlaylist(playlistRequest);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    callback.onUpdatePlaylistSuccess(response.body());
                } else {
                    Log.d(ApplicationConstants.LOGCAT_ID, "Error UpdatePlaylist not successful: " + response.toString());

                    try {
                        callback.onUpdatePlaylistFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.d(ApplicationConstants.LOGCAT_ID, "Error UpdatePlaylist failure: " + Arrays.toString(t.getStackTrace()));
                callback.onUpdatePlaylistFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }

    public synchronized void followPlaylist(String playlistId, final FollowPlaylistCallback callback){

        Call<LikedDTO> call = playlistService.followPlaylist(playlistId);
        call.enqueue(new Callback<LikedDTO>() {
            @Override
            public void onResponse(Call<LikedDTO> call, Response<LikedDTO> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    callback.onFollowPlaylistSuccess(response.body());
                } else {
                    Log.d(ApplicationConstants.LOGCAT_ID, "Error following playlist not successful: " + response.toString());

                    try {
                        callback.onFollowPlaylistFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LikedDTO> call, Throwable t) {
                Log.d(ApplicationConstants.LOGCAT_ID, "Error following playlist failure: " + Arrays.toString(t.getStackTrace()));
                callback.onFollowPlaylistFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }


    public synchronized void getMyFollowedPlaylists(final FollowedPlaylistsCallback callback) {

        Call<List<Playlist>> call = playlistService.getMyFollowedPlaylists();
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    callback.onGetPlaylistsSuccess(response.body());
                } else {
                    Log.d(ApplicationConstants.LOGCAT_ID, "Error MyPlaylists not successful: " + response.toString());

                    try {
                        callback.onGetPlaylistsFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d(ApplicationConstants.LOGCAT_ID, "Error MyPlaylists failure: " + Arrays.toString(t.getStackTrace()));
                callback.onGetPlaylistsFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });
    }
}
