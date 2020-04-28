package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sallefy.R;
import com.sallefy.adapters.AddToPlaylistAdapter;
import com.sallefy.adapters.PlaylistListAdapter;
import com.sallefy.managers.playlists.MyPlaylistsCallback;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;

import java.util.List;

public class AddToPlaylistFragment extends Fragment implements MyPlaylistsCallback {

    private static AddToPlaylistFragment instance;

    private Context mContext;
    private FragmentManager mFragmentManager;
    private ImageButton mBackButton;
    private RecyclerView mPlaylistsRecyclerView;
    private AddToPlaylistAdapter mAddToPlaylistAdapter;
    private Track mTrackToAdd;

    public AddToPlaylistFragment(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        getMyPlaylists();
    }

    public static AddToPlaylistFragment getInstance(Context context, FragmentManager fragmentManager) {
        if (instance == null) instance = new AddToPlaylistFragment(context, fragmentManager);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mTrackToAdd = (Track) bundle.getSerializable("track");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_to_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        mBackButton.setOnClickListener(v -> mFragmentManager.popBackStack());

        mAddToPlaylistAdapter = new AddToPlaylistAdapter(mContext, mFragmentManager, null, mTrackToAdd);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mPlaylistsRecyclerView.setLayoutManager(manager);

    }

    private void initViews(View view) {
        mBackButton = view.findViewById(R.id.add_to_playlist_ib_back);
        mPlaylistsRecyclerView = view.findViewById(R.id.add_to_playlist_rv_playlists);
    }

    private void getMyPlaylists() {
        PlaylistManager.getInstance().getMyPlaylists(mContext, this);
    }
        @Override
    public void onMyPlaylistsReceived(List<Playlist> playlists) {
        mAddToPlaylistAdapter.setPlaylists(playlists);
        mPlaylistsRecyclerView.setAdapter(mAddToPlaylistAdapter);
    }

    @Override
    public void onMyPlaylistsFailure(Throwable throwable) {
        Toast.makeText(mContext, "Error receiving playlists", Toast.LENGTH_SHORT).show();
    }
}
