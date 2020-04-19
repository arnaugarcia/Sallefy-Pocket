package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sallefy.R;
import com.sallefy.adapters.PlaylistTrackListAdapter;
import com.sallefy.adapters.TrackListAdapter;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.model.Playlist;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {

    private FragmentManager fragmentManager;
    private Context context;

    private Playlist playlist;

    private ImageButton ibBack;
    private ImageButton ibOptions;
    private RecyclerView rvSongs;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    public PlaylistFragment(Context context, Playlist playlist, FragmentManager fragmentManager) {
        this.context = context;
        this.playlist = playlist;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        ibBack.setOnClickListener(view1 -> {
            fragmentManager.popBackStack();
        });

        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rvSongs.setLayoutManager(manager);
        PlaylistTrackListAdapter adapter = new PlaylistTrackListAdapter(fragmentManager, context, playlist);
        rvSongs.setAdapter(adapter);
    }

    private void initViews(View view) {
        ibBack = view.findViewById(R.id.ib_back);
        ibOptions = view.findViewById(R.id.ib_options);
        rvSongs = view.findViewById(R.id.rv_songs);
    }
}
