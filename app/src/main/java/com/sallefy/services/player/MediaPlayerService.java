package com.sallefy.services.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.sallefy.model.Playlist;
import com.sallefy.model.Track;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static android.media.AudioManager.STREAM_MUSIC;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.sallefy.services.player.MediaPlayerState.COMPLETED;
import static com.sallefy.services.player.MediaPlayerState.ERROR;
import static com.sallefy.services.player.MediaPlayerState.PAUSED;
import static com.sallefy.services.player.MediaPlayerState.PLAYING;
import static com.sallefy.services.player.MediaPlayerState.PREPARED;
import static com.sallefy.services.player.MediaPlayerState.RESET;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {

    private MediaPlayer mediaPlayer;
    private int resumePosition;

    private AudioManager audioManager;

    private MediaPlayerNotification mediaPlayerNotification;

    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private Track currentTrack; //an object of the currently playing audio

    // Binder given to clients
    private final IBinder iBinder = new MediaPlayerBinder();

    public class MediaPlayerBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Perform one-time setup procedures
        initMediaPlayer();
        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
        //callStateListener();
        //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
        //registerBecomingNoisyReceiver();
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        //Set up MediaPlayer event listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(STREAM_MUSIC);
    }

    public void play(Track track) {
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setDataSource(track.getUrl());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(listener -> {
                    mediaPlayer.start();
                    EventBus.getDefault().postSticky(mediaPlayerEvent(PLAYING));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void play(Playlist playlist) {
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setDataSource(playlist.getTracks().get(0).getUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            EventBus.getDefault().postSticky(mediaPlayerEvent(PLAYING));
        }
    }

    public void stop() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            EventBus.getDefault().postSticky(mediaPlayerEvent(PAUSED));
        }
    }

    @NotNull
    private MediaPlayerEvent.StateChanged mediaPlayerEvent(MediaPlayerState paused) {
        return new MediaPlayerEvent.StateChanged(paused);
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
            EventBus.getDefault().postSticky(mediaPlayerEvent(PAUSED));
        }
    }

    public void resume() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
            EventBus.getDefault().postSticky(mediaPlayerEvent(PLAYING));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Invoked when playback of a media source has completed.
        stop();
        //stop the service
        stopSelf();
        EventBus.getDefault().postSticky(mediaPlayerEvent(COMPLETED));
    }

    //Handle errors
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        EventBus.getDefault().postSticky(mediaPlayerEvent(ERROR));
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info.
        // makeText(this, "OnInfo", LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
        EventBus.getDefault().postSticky(mediaPlayerEvent(PREPARED));
        makeText(this, "OnPrepared", LENGTH_SHORT).show();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.
        // makeText(this, "OnSeekComplete", LENGTH_SHORT).show();
    }

    @Override
    public void onAudioFocusChange(int focusState) {
        //Invoked when the audio focus of the system is updated.
        // makeText(this, "OnAudioFocusChange", LENGTH_SHORT).show();
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) initMediaPlayer();
                else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        makeText(this, "Destroying MediaPlayerService", LENGTH_SHORT).show();
        /*if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
        removeAudioFocus();

        mediaPlayerNotification.removeNotification();*/

    }

    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Creates the new mediaPlayerNotification object
        mediaPlayerNotification = new MediaPlayerNotification(this, mediaSession);

        //Set mediaSession's MetaData
        mediaPlayerNotification.updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();
                resume();
                mediaPlayerNotification.buildNotification(PlaybackStatus.PLAYING);
                EventBus.getDefault().postSticky(mediaPlayerEvent(PLAYING));
            }

            @Override
            public void onPause() {
                super.onPause();
                pause();
                mediaPlayerNotification.buildNotification(PlaybackStatus.PAUSED);
                EventBus.getDefault().postSticky(mediaPlayerEvent(PAUSED));
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                skipToNext();
                mediaPlayerNotification.updateMetaData();
                mediaPlayerNotification.buildNotification(PlaybackStatus.PLAYING);
                EventBus.getDefault().postSticky(mediaPlayerEvent(PLAYING));
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();
                mediaPlayerNotification.updateMetaData();
                mediaPlayerNotification.buildNotification(PlaybackStatus.PLAYING);
                EventBus.getDefault().postSticky(mediaPlayerEvent(PLAYING));
            }

            @Override
            public void onStop() {
                super.onStop();
                // removeNotification();
                //Stop the service
                EventBus.getDefault().postSticky(mediaPlayerEvent(COMPLETED));
                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            makeText(this, "onStartCommand", LENGTH_SHORT).show();
            //Load data from SharedPreferences
            //StorageUtil storage = new StorageUtil(getApplicationContext());

            /*if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                currentTrack = audioList.get(audioIndex);
            } else {
                stopSelf();
            }*/
        } catch (NullPointerException e) {
            stopSelf();
        }

        //Request audio focus
        if (!requestAudioFocus()) {
            //Could not gain focus
            stopSelf();
        }

        if (mediaSessionManager == null) {
            try {
                initMediaSession();
                initMediaPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
            mediaPlayerNotification.buildNotification(PlaybackStatus.PLAYING);
        }

        //Handle Intent action from MediaSession.TransportControls
        mediaPlayerNotification.handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void skipToNext() {

        /*if (audioIndex == audioList.size() - 1) {
            //if last in playlist
            audioIndex = 0;
            currentTrack = audioList.get(audioIndex);
        } else {
            //get next in playlist
            currentTrack = audioList.get(++audioIndex);
        }*/

        stop();
        //reset mediaPlayer
        EventBus.getDefault().postSticky(mediaPlayerEvent(RESET));
        mediaPlayer.reset();
        initMediaPlayer();
    }

    private void skipToPrevious() {

       /* if (audioIndex == 0) {
            //if first in playlist
            //set index to the last of audioList
            audioIndex = audioList.size() - 1;
            currentTrack = audioList.get(audioIndex);
        } else {
            //get previous in playlist
            currentTrack = audioList.get(--audioIndex);
        }*/

        stop();
        //reset mediaPlayer
        EventBus.getDefault().postSticky(mediaPlayerEvent(RESET));
        mediaPlayer.reset();
        initMediaPlayer();
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        //Focus gained
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        //Could not gain focus
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

}