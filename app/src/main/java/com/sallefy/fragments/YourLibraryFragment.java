package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sallefy.R;
import com.sallefy.adapters.PlaylistListAdapter;
import com.sallefy.managers.playlists.PlaylistCallback;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class YourLibraryFragment extends Fragment implements PlaylistCallback {

    private static YourLibraryFragment instance;
    private Context context;

    private RecyclerView rvPlaylists;
    private List<Playlist> playlists;

    public YourLibraryFragment(Context context) {
        this.context = context;
        this.playlists = new ArrayList<>();
        getMyPlaylists();
    }

    public static YourLibraryFragment getInstance(Context context) {
        if (instance == null) instance = new YourLibraryFragment(context);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews(View view) {
        rvPlaylists = view.findViewById(R.id.rv_playlists);
    }

    private void getMyPlaylists() {
        PlaylistManager.getInstance().getMyPlaylists(context, this);
    }

    @Override
    public void onMyPlaylistsReceived(List<Playlist> playlists) {
        this.playlists = playlists;
        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rvPlaylists.setLayoutManager(manager);
        PlaylistListAdapter adapter = new PlaylistListAdapter(context, this.playlists);
        rvPlaylists.setAdapter(adapter);
    }

    @Override
    public void onMyPlaylistsFailure(Throwable throwable) {
        Toast.makeText(context, "Error receiving playlists", Toast.LENGTH_SHORT).show();
    }
}
