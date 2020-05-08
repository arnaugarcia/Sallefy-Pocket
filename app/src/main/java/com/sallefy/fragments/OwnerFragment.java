package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.sallefy.R;
import com.sallefy.model.User;

public class OwnerFragment extends Fragment {

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

}
