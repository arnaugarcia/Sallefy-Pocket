package com.sallefy.services.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import static android.net.wifi.WifiManager.WIFI_MODE_FULL_LOW_LATENCY;
import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String ACTION_PLAY = "com.example.action.PLAY";
    MediaPlayer mediaPlayer = null;

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            mediaPlayer = new MediaPlayer(); // initialize it here
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync(); // prepare async to not block main thread

            // Locks
            mediaPlayer.setWakeMode(getApplicationContext(), PARTIAL_WAKE_LOCK);

            WifiManager.WifiLock wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WIFI_MODE_FULL_LOW_LATENCY, "mylock");
            wifiLock.acquire();
        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }


}
