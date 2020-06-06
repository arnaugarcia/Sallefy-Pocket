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
import com.sallefy.services.player.MediaPlayerEvent;
import com.sallefy.services.player.MediaPlayerService;
import com.sallefy.services.player.MediaPlayerState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

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

    private MediaPlayerService player;
    boolean serviceBound = false;
    private MediaPlayerState playerState;

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
        checkAndStartMediaPlayerService();
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
        new TabLayoutMediator(tabLayout, viewPager,
                ((tab, position) -> tab.setText((position == 0) ? "Playlists" : "Tracks"))
        ).attach();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void checkAndStartMediaPlayerService() {
        // EventBus.getDefault().register(context);
        playerState = EventBus.getDefault().getStickyEvent(MediaPlayerEvent.StateChanged.class).currentState;
        if (!serviceBound) {
            // makeText(this, "[Track Activity] - Media Player service is not active", LENGTH_SHORT).show();
            Intent playerIntent = new Intent(context, MediaPlayerService.class);
            context.startService(playerIntent);
            context.bindService(playerIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    @Subscribe(threadMode = MAIN)
    public void onMediaPlayerStateChanged(MediaPlayerEvent.StateChanged event) {
        playerState = event.currentState;
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
        player.play(track);
    }

    @Override
    public void onTrackLiked(Track track) {

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder)service;
            player = binder.getService();
            // mBoundService.setCallback(SongsFragment.this);
            serviceBound = true;
            // updateSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
}
