package com.sallefy.activity;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sallefy.R;
import com.sallefy.services.player.PlayerService;

public class TrackActivity extends AppCompatActivity  {

    private PlayerService playerService;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        initViews();
        /*Intent intent = new Intent(this, PlayerService.class);
        this.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);*/
    }

    private void initViews() {
        handler = new Handler();
        seekBar = findViewById(R.id.activity_track_seekBar);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder)service;
            playerService = binder.getService();
            //updateSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //mServiceBound = false;
        }
    };

    public void updateSeekBar() {
        System.out.println("max duration: " + playerService.getMaxDuration());
        System.out.println("progress:" + playerService.getCurrentPosition());
        seekBar.setMax(playerService.getMaxDuration());
        seekBar.setProgress(playerService.getCurrentPosition());

        if(playerService.isPlaying()) {
            runnable = this::updateSeekBar;
            handler.postDelayed(runnable, 1000);
        }
    }
}
