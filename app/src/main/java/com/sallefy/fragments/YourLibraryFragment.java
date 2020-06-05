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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sallefy.R;
import com.sallefy.adapters.YourLibraryAdapter;
import com.sallefy.adapters.callbacks.TrackListCallback;
import com.sallefy.managers.playlists.MyPlaylistsCallback;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.managers.tracks.MyTracksCallback;
import com.sallefy.managers.tracks.TrackManager;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;
import com.sallefy.services.player.MediaPlayerService;

import java.util.List;

public class YourLibraryFragment extends Fragment
        implements MyPlaylistsCallback, MyTracksCallback, TrackListCallback {

    private static YourLibraryFragment instance;
    private FragmentManager fragmentManager;
    private Context context;

    private ImageButton ibSettings;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private YourLibraryAdapter yourLibraryAdapter;
    private TextView mLibraryTitleTextView;

    private MediaPlayerService mBoundService;
    private boolean mServiceBound = false;

    public YourLibraryFragment(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.yourLibraryAdapter = new YourLibraryAdapter(this, context, fragmentManager);
        getMyPlaylists();
        getMyTracks();
    }

    public static YourLibraryFragment getInstance(Context context, FragmentManager fragmentManager) {
        if (instance == null) instance = new YourLibraryFragment(context, fragmentManager);
        if (instance.fragmentManager != fragmentManager) instance.fragmentManager = fragmentManager;
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getContext(), MediaPlayerService.class);
        // getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_your_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        ibSettings.setOnClickListener(view1 -> openProfileFragment());

        viewPager.setAdapter(yourLibraryAdapter);
        int colorAccent = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
        int colorTextPrimaryVariant = ResourcesCompat.getColor(getResources(), R.color.colorTextPrimaryVariant, null);
        tabLayout.setSelectedTabIndicatorColor(colorAccent);
        tabLayout.setTabTextColors(colorTextPrimaryVariant, colorAccent);
        //TODO extract hardcoded text to @strings
        new TabLayoutMediator(tabLayout, viewPager,
                ((tab, position) -> tab.setText((position == 0) ? "Playlists" : "Tracks"))
        ).attach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyPlaylists();
        getMyTracks();
    }

    private void initViews(View view) {
        ibSettings = view.findViewById(R.id.ib_settings);
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        mLibraryTitleTextView = view.findViewById(R.id.tv_your_library_title);
    }

    private void getMyPlaylists() {
        PlaylistManager.getInstance().getMyPlaylists(context, this);
    }

    private void getMyTracks() {
        TrackManager.getInstance().getMyTracks(context, this);
    }

    private void openProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment(context);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_manager, profileFragment, "profileFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMyPlaylistsReceived(List<Playlist> playlists) {
        yourLibraryAdapter.setPlaylists(playlists);
        viewPager.setAdapter(yourLibraryAdapter);
    }

    @Override
    public void onMyPlaylistsFailure(Throwable throwable) {
        Toast.makeText(context, "Error receiving playlists", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMyTracksReceived(List<Track> tracks) {
        yourLibraryAdapter.setTracks(tracks);
        viewPager.setAdapter(yourLibraryAdapter);
    }

    @Override
    public void onMyTracksFailure(Throwable throwable) {
        Toast.makeText(context, "Error receiving tracks", Toast.LENGTH_SHORT).show();
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
            // mBoundService.setCallback(SongsFragment.this);
            mServiceBound = true;
            // updateSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };
}
