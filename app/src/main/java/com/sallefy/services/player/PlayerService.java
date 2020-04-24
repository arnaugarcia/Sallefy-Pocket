package com.sallefy.services.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sallefy.model.Playlist;
import com.sallefy.model.Track;

import java.util.List;

import static android.net.wifi.WifiManager.WIFI_MODE_FULL_LOW_LATENCY;
import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private static final String ACTION_PLAY = "com.example.action.PLAY";
    private final IBinder mBinder = new PlayerBinder();
    MediaPlayer mediaPlayer = null;

    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // if (intent.getAction().equals(ACTION_PLAY)) {
            mediaPlayer = new MediaPlayer(); // initialize it here
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync(); // prepare async to not block main thread

            // Locks
            mediaPlayer.setWakeMode(getApplicationContext(), PARTIAL_WAKE_LOCK);

            WifiManager.WifiLock wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WIFI_MODE_FULL_LOW_LATENCY, "mylock");
            wifiLock.acquire();
       // }
        return START_REDELIVER_INTENT;
    }

    public void togglePlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void play(Track track) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(mp -> {
            // updateTrack(1);
        });

        try {
            mediaPlayer.setDataSource(track.getUrl());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                System.out.println("Entra en el prepared");

                /*if (mCallback != null) {
                    System.out.println("Entra en el callback");
                    mCallback.onMusicPlayerPrepared();
                }*/
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void play(List<Track> tracks) {

    }

    public void play(Playlist playlist) {

    }

    public int getMaxDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Called when MediaPlayer is ready
     */
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }


}
