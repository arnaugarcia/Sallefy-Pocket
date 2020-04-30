package com.sallefy.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sallefy.R;
import com.sallefy.adapters.TrackListAdapter;
import com.sallefy.adapters.callbacks.TrackListCallback;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;
import com.sallefy.services.player.MediaPlayerService;

public class PlaylistFragment extends Fragment implements TrackListCallback {

    private FragmentManager fragmentManager;
    private Context context;

    private Playlist playlist;

    private ImageButton ibBack;
    private ImageButton ibOptions;
    private RecyclerView rvSongs;

    private ConstraintLayout playlistDataLayout;
    private TextView tvPlaylistTitle;
    private TextView tvPlaylistDescription;
    private CircularImageView ivPlaylistThumbnail;

    private MediaPlayerService mBoundService;
    private boolean mServiceBound = false;

    public PlaylistFragment() {
    }

    public PlaylistFragment(Context context, Playlist playlist, FragmentManager fragmentManager) {
        this.context = context;
        this.playlist = playlist;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getContext(), MediaPlayerService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        tvPlaylistTitle.setText(playlist.getName());
        tvPlaylistDescription.setText(playlist.getDescription());
        if (playlist.getThumbnail() != null) {
            Glide.with(context)
                    .asBitmap()
                    .placeholder(R.drawable.application_logo)
                    .load(playlist.getThumbnail())
                    .into(ivPlaylistThumbnail);
        }

        ibBack.setOnClickListener(view1 -> fragmentManager.popBackStack());

        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rvSongs.setLayoutManager(manager);
        TrackListAdapter adapter = new TrackListAdapter(this, context, playlist);
        rvSongs.setAdapter(adapter);
    }

    private void initViews(View view) {
        ibBack = view.findViewById(R.id.ib_back);
        ibOptions = view.findViewById(R.id.ib_options);
        rvSongs = view.findViewById(R.id.rv_songs);

        playlistDataLayout = view.findViewById(R.id.playlist_data);
        tvPlaylistTitle = view.findViewById(R.id.tv_playlist_title);
        tvPlaylistDescription = view.findViewById(R.id.tv_playlist_description);
        ivPlaylistThumbnail = view.findViewById(R.id.iv_playlist_thumbnail);
    }

    @Override
    public void onTrackSelected(Track track) {
        mBoundService.play(track);
    }

    @Override
    public void onTrackLiked(Track track) {

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder)service;
            mBoundService = binder.getService();
            //mBoundService.setCallback(SongsFragment.this);
            mServiceBound = true;
            //updateSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };
}
