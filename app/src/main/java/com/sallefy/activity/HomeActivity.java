package com.sallefy.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sallefy.R;
import com.sallefy.fragments.HomeFragment;
import com.sallefy.fragments.SearchFragment;
import com.sallefy.fragments.YourLibraryFragment;
import com.sallefy.services.player.MediaPlayerEvent;
import com.sallefy.services.player.MediaPlayerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class HomeActivity extends FragmentActivity {

    private BottomNavigationView mBottomNavigationView;
    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;
    private TextView tvMusicNav;
    private ImageView btnPlay;

    private MediaPlayerService player;
    boolean mServiceBound = false;

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            player = binder.getService();
            mServiceBound = true;
            makeText(getApplicationContext(), "[Home Activity] - Service disconnected", LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        initMusicNavView();
        setInitialFragment();
        checkAndStartMediaPlayerService();
    }

    private void checkAndStartMediaPlayerService() {
        if (!mServiceBound) {
            // makeText(this, "[Track Activity] - Media Player service is not active", LENGTH_SHORT).show();
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startService(playerIntent);
            bindService(playerIntent, mServiceConnection, BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }

    /*private void togglePlayButton() {
        if (playerState.isPlaying()) {
            btnPlay.setImageResource(R.drawable.ic_pause);
        } else {
            btnPlay.setImageResource(R.drawable.ic_play);
        }
    }*/

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaPlayerStateChanged(MediaPlayerEvent.StateChanged event) {
        switch (event.currentState) {
            case PLAYING:
                btnPlay.setImageResource(R.drawable.ic_pause);
                break;
            case PAUSED:
                btnPlay.setImageResource(R.drawable.ic_play);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mServiceBound){
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
        EventBus.getDefault().unregister(this);

    }

    private void startStreamingService() {
        Intent intent = new Intent(this, HomeActivity.class);
        startService(intent);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void initMusicNavView() {
        tvMusicNav = findViewById(R.id.music_nav_title);
        tvMusicNav.setOnClickListener(v -> startActivity(new Intent(this, TrackActivity.class)));

        btnPlay = findViewById(R.id.music_nav_play);
        btnPlay.setOnClickListener(listener -> {
            makeText(getApplicationContext(), "Toggle track", LENGTH_SHORT).show();
            player.pause();
            // togglePlayButton();
        });
    }

    private void initViews() {
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.action_home:
                    fragment = HomeFragment.getInstance();
                    break;
                case R.id.action_search:
                    fragment = SearchFragment.getInstance();
                    break;
                case R.id.action_your_library:
                    fragment = YourLibraryFragment.getInstance(getApplicationContext(), mFragmentManager);
                    break;
            }
            changeFragment(fragment);
            return true;
        });
    }


    private void changeFragment(Fragment fragment) {
        String fragmentTag = ((Object) fragment).getClass().getName();
        Fragment currentFragment = mFragmentManager.findFragmentByTag(fragmentTag);
        try {
            if (currentFragment != null) {
                if (!currentFragment.isVisible()) {
                    if (fragment.getArguments() != null) {
                        currentFragment.setArguments(fragment.getArguments());
                    }
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_manager, currentFragment, fragmentTag)
                            .addToBackStack(null)
                            .commit();
                }
            } else {
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_manager, fragment, fragmentTag)
                        .addToBackStack(null)
                        .commit();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }

    private void setInitialFragment() {
        mTransaction.add(R.id.fragment_manager, HomeFragment.getInstance());
        mTransaction.commit();
    }

}
