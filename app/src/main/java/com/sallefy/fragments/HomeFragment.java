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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements
        MostFollowedUsersCallback,
        MostFollowedPlaylistsCallback,
        MostPlayedTracksCallback {

    private static HomeFragment instance;
    private Context context;

    private static final String TAG = "HomeFragment";

    private RecyclerView rvArtists;
    private RecyclerView rvPlaylists;
    private RecyclerView rvTracks;

    public HomeFragment(Context context) {
        this.context = context;
    }

    public static HomeFragment getInstance(Context context) {
        if (instance == null) instance = new HomeFragment(context);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMostFollowedUsers();
        getMostFollowedPlaylists();
        getMostFollowedTracks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view) {
        rvArtists = view.findViewById(R.id.rv_artists);
        rvPlaylists = view.findViewById(R.id.rv_playlists);
        rvTracks = view.findViewById(R.id.rv_tracks);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getMostFollowedUsers() {
        UserManager.getInstance().getMostFollowedUsers(getContext(), this);
    }

    private void getMostFollowedPlaylists() {
        PlaylistManager.getInstance().getMostFollowedPlaylists(getContext(), this);
    }

    private void getMostFollowedTracks() {
        TrackManager.getInstance().getMostPlayedTracks(getContext(), this);
    }

    @Override
    public void onMostFollowedUsersSuccess(List<User> users) {
        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        FeaturedUserListAdapter adapter = new FeaturedUserListAdapter(context, users);
        rvArtists.setLayoutManager(manager);
        rvArtists.setAdapter(adapter);
    }

    @Override
    public void onMostFollowedUsersFailure(Throwable throwable) {
        Log.d(TAG, "onPopularUsersFailure: " + throwable.getMessage());
        Toast.makeText(context, "Error receiving users", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMostFollowedPlaylistsSuccess(List<Playlist> playlists) {
        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        FeaturedPlaylistListAdapter adapter = new FeaturedPlaylistListAdapter(context, playlists);
        rvPlaylists.setLayoutManager(manager);
        rvPlaylists.setAdapter(adapter);
    }

    @Override
    public void onMostFollowedPlaylistsFailure(Throwable throwable) {
        Log.d(TAG, "onMostFollowedPlaylistsFailure: " + throwable.getMessage());
        Toast.makeText(context, "Error receiving playlists", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMostPlayedTracksSuccess(List<Track> tracks) {
        Log.d(TAG, "onMostFollowedTracksSuccess: " + tracks);
        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        FeaturedTrackListAdapter adapter = new FeaturedTrackListAdapter(context, tracks);
//        rvPlaylists.setLayoutManager(manager);
//        rvPlaylists.setAdapter(adapter);
    }

    @Override
    public void onMostPlayedTracksFailure(Throwable throwable) {
        Log.d(TAG, "onMostFollowedTracksFailure: " + throwable.getMessage());
        Toast.makeText(context, "Error receiving tracks", Toast.LENGTH_SHORT).show();
    }
}
