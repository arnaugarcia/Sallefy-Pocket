package com.sallefy.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sallefy.R;
import com.sallefy.model.Track;
import com.sallefy.services.player.MediaPlayerEvent;
import com.sallefy.services.player.MediaPlayerService;
import com.sallefy.services.player.MediaPlayerState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.sallefy.services.player.MediaPlayerState.PAUSED;
import static com.sallefy.services.player.MediaPlayerState.PLAYING;
import static java.util.Objects.isNull;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class TrackActivity extends AppCompatActivity {

    private ImageButton btnPlay;

    private MediaPlayerService player;
    boolean serviceBound = false;
    private MediaPlayerState playerState;
    private TextView tvTitleSong;
    private TextView tvArtist;

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            player = binder.getService();
            serviceBound = true;
            Track currentTrack = player.getCurrentTrack();
            if (!isNull(currentTrack)) {
                updateViewBy(currentTrack);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        checkAndStartMediaPlayerService();
        initViews();

    }

    private void checkAndStartMediaPlayerService() {
        EventBus.getDefault().register(this);
        playerState = EventBus.getDefault().getStickyEvent(MediaPlayerEvent.StateChanged.class).currentState;
        if (!serviceBound) {
            // makeText(this, "[Track Activity] - Media Player service is not active", LENGTH_SHORT).show();
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void initViews() {
        btnPlay = findViewById(R.id.activity_track_play_btn);
        tvTitleSong = findViewById(R.id.tv_activity_song_title);
        tvArtist = findViewById(R.id.tv_activity_artist_name);
        if (playerState == PLAYING) btnPlay.setImageResource(R.drawable.ic_pause);
        if (playerState == PAUSED) btnPlay.setImageResource(R.drawable.ic_play);

        btnPlay.setOnClickListener(listener -> {
            if (playerState == PLAYING) {
                player.stop();
            } else {
                player.resume();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = MAIN)
    public void onMediaPlayerStateChanged(MediaPlayerEvent.StateChanged event) {
        switch (event.currentState) {
            case PLAYING:
                Track track = player.getCurrentTrack();
                updateViewBy(track);
                btnPlay.setImageResource(R.drawable.ic_pause);
                break;
            case PAUSED:
                btnPlay.setImageResource(R.drawable.ic_play);
                break;
        }
    }

    private void updateViewBy(Track track) {
        tvTitleSong.setText(track.getName());
        tvArtist.setText(track.getUserLogin());
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }

}
