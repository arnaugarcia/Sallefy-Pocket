package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.sallefy.R;
import com.sallefy.adapters.FeaturedPlaylistListAdapter;
import com.sallefy.adapters.TrackListAdapter;
import com.sallefy.adapters.callbacks.TrackListCallback;
import com.sallefy.managers.user.PlaylistsByLoginCallback;
import com.sallefy.managers.user.TracksByLoginCallback;
import com.sallefy.managers.user.UserManager;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;
import com.sallefy.model.User;

import java.util.List;

import static org.greenrobot.eventbus.EventBus.TAG;

public class OwnerFragment extends Fragment implements TracksByLoginCallback, PlaylistsByLoginCallback, TrackListCallback {

    private Context mContext;
    private FragmentManager mFragmentManager;
    private User mUser;

    private ImageView mOwnerImage;
    private ImageButton mImageButtonBack;
    private MaterialTextView mOwnerName;
    private MaterialTextView mOwnerTrackNumber;
    private MaterialTextView mPlaylistsViewAll;
    private RecyclerView mOwnerPlaylists;
    private MaterialTextView mTracksViewAll;
    private RecyclerView mOwnerTracks;

    public OwnerFragment(Context mContext, FragmentManager mFragmentManager, User mUser) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
        this.mUser = mUser;
        getTracksByLogin(mUser.getLogin());
        getPlaylistsByLogin(mUser.getLogin());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        mImageButtonBack.setOnClickListener(v -> mFragmentManager.popBackStack());

        //mOwnerImage
        mOwnerName.setText(mUser.getLogin());
        mOwnerTrackNumber.setText(mUser.getTracks());
    }

    private void initViews(View view) {
        mOwnerImage = view.findViewById(R.id.iv_owner_image);
        mImageButtonBack = view.findViewById(R.id.ib_owner_back);
        mOwnerName = view.findViewById(R.id.tv_owner_name);
        mOwnerTrackNumber = view.findViewById(R.id.tv_owner_track_number);
        mPlaylistsViewAll = view.findViewById(R.id.tv_owner_playlists_view_all);
        mOwnerPlaylists = view.findViewById(R.id.rv_owner_playlists);
        mTracksViewAll = view.findViewById(R.id.tv_owner_tracks_view_all);
        mOwnerTracks = view.findViewById(R.id.rv_owner_tracks);
    }

    private void getTracksByLogin(String login) {
        UserManager.getInstance().getTracksByLogin(mContext, login, this);
    }

    private void getPlaylistsByLogin(String login){
        UserManager.getInstance().getPlaylistsByLogin(mContext, login, this);
    }

    @Override
    public void onUserTracksReceived(List<Track> tracks) {
        LinearLayoutManager trackManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mOwnerTracks.setLayoutManager(trackManager);
        TrackListAdapter mTrackListAdapter = new TrackListAdapter(this, mContext, tracks, mFragmentManager);
        mOwnerTracks.setAdapter(mTrackListAdapter);
    }

    @Override
    public void onUserTracksFailure(Throwable throwable) {
        Log.d(TAG, "onUserTracksFailure: " + throwable.getMessage());
        Toast.makeText(mContext, "Error receiving tracks", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserPlaylistsReceived(List<Playlist> playlists) {
        LinearLayoutManager playlistManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        mOwnerPlaylists.setLayoutManager(playlistManager);
        FeaturedPlaylistListAdapter mPlaylistListAdapter = new FeaturedPlaylistListAdapter(mContext, playlists);
        mOwnerPlaylists.setAdapter(mPlaylistListAdapter);
    }

    @Override
    public void onUserPlaylistsFailure(Throwable throwable) {
        Log.d(TAG, "onUserPlaylistsFailure: " + throwable.getMessage());
        Toast.makeText(mContext, "Error receiving playlists", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackLiked(Track track) {

    }
}
