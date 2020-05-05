package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sallefy.R;
import com.sallefy.adapters.FeaturedPlaylistListAdapter;
import com.sallefy.adapters.FeaturedTrackListAdapter;
import com.sallefy.adapters.FeaturedUserListAdapter;
import com.sallefy.managers.playlists.MostFollowedPlaylistsCallback;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.managers.tracks.MostPlayedTracksCallback;
import com.sallefy.managers.tracks.TrackManager;
import com.sallefy.managers.user.MostFollowedUsersCallback;
import com.sallefy.managers.user.UserManager;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;
import com.sallefy.model.User;
import com.sallefy.viewmodels.HomeFragmentViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements
        MostFollowedUsersCallback,
        MostFollowedPlaylistsCallback,
        MostPlayedTracksCallback {

    private static final String TAG = "HomeFragment";

    private static HomeFragment instance;
    private Context mContext;

    private HomeFragmentViewModel mHomeFragmentViewModel;

    private LinearLayoutManager mLayoutManager;
    FeaturedUserListAdapter mUserAdapter;
    FeaturedPlaylistListAdapter mPlaylistAdapter;
    FeaturedTrackListAdapter mTrackAdapter;

    private RecyclerView rvArtists;
    private RecyclerView rvPlaylists;
    private RecyclerView rvTracks;

    public HomeFragment(Context mContext) {
        this.mContext = mContext;
    }

    public static HomeFragment getInstance(Context context) {
        if (instance == null) instance = new HomeFragment(context);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);

        mHomeFragmentViewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
        mHomeFragmentViewModel.setContext(mContext);

        mHomeFragmentViewModel.init();

        mHomeFragmentViewModel.getUsers()
                .observe(getViewLifecycleOwner(), users -> mUserAdapter.notifyDataSetChanged());

//        mHomeFragmentViewModel.getTracks()
//                .observe(this, tracks -> mTrackAdapter.notifyDataSetChanged());
//
//        mHomeFragmentViewModel.getPlaylists()
//                .observe(this, playlists -> mPlaylistAdapter.notifyDataSetChanged());

        initRecyclerViews();

        getMostFollowedUsers();
        getMostFollowedPlaylists();
        getMostPlayedTracks();
    }

    private void initViews(View view) {
        rvArtists = view.findViewById(R.id.rv_artists);
        rvPlaylists = view.findViewById(R.id.rv_playlists);
        rvTracks = view.findViewById(R.id.rv_tracks);
    }

    private void initRecyclerViews() {
        mUserAdapter = new FeaturedUserListAdapter(mContext, mHomeFragmentViewModel.getUsers().getValue());
//        mTrackAdapter = new FeaturedTrackListAdapter(mContext, mHomeFragmentViewModel.getTracks().getValue());
//        mPlaylistAdapter = new FeaturedPlaylistListAdapter(mContext, mHomeFragmentViewModel.getPlaylists().getValue());

//        rvTracks.setLayoutManager(mLayoutManager);
//        rvTracks.setAdapter(mTrackAdapter);
        rvArtists.setLayoutManager(mLayoutManager);
        rvArtists.setAdapter(mUserAdapter);
//        rvPlaylists.setLayoutManager(mLayoutManager);
//        rvPlaylists.setAdapter(mPlaylistAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        getMostFollowedUsers();
        getMostFollowedPlaylists();
        getMostPlayedTracks();
    }

    private void getMostFollowedUsers() {
        UserManager.getInstance().getMostFollowedUsers(getContext(), this);
    }

    private void getMostFollowedPlaylists() {
        PlaylistManager.getInstance().getMostFollowedPlaylists(getContext(), this);
    }

    private void getMostPlayedTracks() {
        TrackManager.getInstance().getMostPlayedTracks(getContext(), this);
    }

    @Override
    public void onMostFollowedUsersSuccess(List<User> users) {
        mUserAdapter = new FeaturedUserListAdapter(mContext, users);
        rvArtists.setLayoutManager(mLayoutManager);
        rvArtists.setAdapter(mUserAdapter);
    }

    @Override
    public void onMostFollowedUsersFailure(Throwable throwable) {
        Log.d(TAG, "onPopularUsersFailure: " + throwable.getMessage());
        Toast.makeText(mContext, "Error receiving users", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMostFollowedPlaylistsSuccess(List<Playlist> playlists) {
        mPlaylistAdapter = new FeaturedPlaylistListAdapter(mContext, playlists);
        rvPlaylists.setLayoutManager(mLayoutManager);
        rvPlaylists.setAdapter(mPlaylistAdapter);
    }

    @Override
    public void onMostFollowedPlaylistsFailure(Throwable throwable) {
        Log.d(TAG, "onMostFollowedPlaylistsFailure: " + throwable.getMessage());
        Toast.makeText(mContext, "Error receiving playlists", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMostPlayedTracksSuccess(List<Track> tracks) {
        mTrackAdapter = new FeaturedTrackListAdapter(mContext, tracks);
        rvTracks.setLayoutManager(mLayoutManager);
        rvTracks.setAdapter(mTrackAdapter);
    }

    @Override
    public void onMostPlayedTracksFailure(Throwable throwable) {
        Log.d(TAG, "onMostFollowedTracksFailure: " + throwable.getMessage());
        Toast.makeText(mContext, "Error receiving tracks", Toast.LENGTH_SHORT).show();
    }
}
