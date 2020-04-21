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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sallefy.R;
import com.sallefy.adapters.TrackListAdapter;
import com.sallefy.adapters.callbacks.TrackListCallback;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;
import com.sallefy.services.player.PlayerService;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment implements TrackListCallback {

    private FragmentManager fragmentManager;
    private Context context;

    private Playlist playlist;

    private ImageButton ibBack;
    private ImageButton ibOptions;
    private RecyclerView rvSongs;

    private PlayerService mBoundService;

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
        Intent intent = new Intent(getContext(), PlayerService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
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
        TrackListAdapter adapter = new TrackListAdapter(this, context, playlist.getTracks());
        rvSongs.setAdapter(adapter);
    }

    private void initViews(View view) {
        ibBack = view.findViewById(R.id.ib_back);
        ibOptions = view.findViewById(R.id.ib_options);
        rvSongs = view.findViewById(R.id.rv_songs);
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
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder)service;
            mBoundService = binder.getService();
            //mBoundService.setCallback(SongsFragment.this);
            //mServiceBound = true;
            //updateSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //mServiceBound = false;
        }
    };
}
