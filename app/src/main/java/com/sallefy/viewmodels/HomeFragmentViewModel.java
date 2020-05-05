package com.sallefy.viewmodels;

import android.content.Context;

import com.sallefy.model.Playlist;
import com.sallefy.model.Track;
import com.sallefy.model.User;
import com.sallefy.repositories.UserRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeFragmentViewModel extends ViewModel {

    private Context mContext;

    private MutableLiveData<List<User>> mUsers;
    private MutableLiveData<List<Playlist>> mPlaylists;
    private MutableLiveData<List<Track>> mTracks;

    private UserRepository mUserRepository;

    public void init() {
        if (mUsers != null) return;

        mUserRepository = UserRepository.getInstance(mContext);
        mUsers = mUserRepository.getUsers();
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public MutableLiveData<List<User>> getUsers() {
        return mUsers;
    }

    public MutableLiveData<List<Playlist>> getPlaylists() {
        return mPlaylists;
    }

    public MutableLiveData<List<Track>> getTracks() {
        return mTracks;
    }
}
