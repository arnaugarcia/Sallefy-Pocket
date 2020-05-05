package com.sallefy.repositories;

import android.content.Context;

import com.sallefy.managers.playlists.MostFollowedPlaylistsCallback;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.managers.tracks.MostPlayedTracksCallback;
import com.sallefy.managers.tracks.TrackManager;
import com.sallefy.managers.user.MostFollowedUsersCallback;
import com.sallefy.managers.user.UserManager;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;
import com.sallefy.model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class UserRepository implements MostFollowedUsersCallback {

    private static UserRepository instance;
    private Context mContext;

    private List<User> mUsers = new ArrayList<>();

    public UserRepository(Context context) {
        this.mContext = context;
    }

    public static UserRepository getInstance(Context context) {
        if (instance == null) instance = new UserRepository(context);
        return instance;
    }

    public MutableLiveData<List<User>> getUsers() {
        getMostFollowedUsers();

        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.setValue(mUsers);
        return data;
    }

    private void getMostFollowedUsers() {
        UserManager.getInstance().getMostFollowedUsers(mContext, this);
    }

    @Override
    public void onMostFollowedUsersSuccess(List<User> users) {
        this.mUsers = users;
    }

    @Override
    public void onMostFollowedUsersFailure(Throwable throwable) {

    }
}
