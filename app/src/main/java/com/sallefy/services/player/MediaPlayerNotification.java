package com.sallefy.services.player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;

import com.sallefy.R;

class MediaPlayerNotification {

    public static final String ACTION_PLAY = "com.sallefy.mediaplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.sallefy.mediaplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.sallefy.mediaplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.sallefy.mediaplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.sallefy.mediaplayer.ACTION_STOP";

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;

    private final Context context;

    private final MediaControllerCompat.TransportControls transportControls;
    private final MediaSessionCompat mediaSession;

    MediaPlayerNotification(Context context, MediaSessionCompat mediaSession) {
        this.context = context;
        this.mediaSession = mediaSession;
        this.transportControls = mediaSession.getController().getTransportControls();
    }

    void buildNotification(PlaybackStatus playbackStatus) {

        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = android.R.drawable.ic_media_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }

        // Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.image); //replace with your own image

        // Create a new Notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setShowWhen(false)
                // Set the Notification style
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compact notification view.
                        .setShowActionsInCompactView(0, 1, 2))
                // Set the Notification color
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                // Set the large and small icons
                // .setLargeIcon(largeIcon)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                // Set Notification content information
                // .setContentText(currentTrack.getUser().getLogin())
                .setContentText("Owner")
                //.setContentTitle(currentTrack.getAlbum())
                .setContentInfo("track name")
                // Add playback actions
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(context, MediaPlayerService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(context, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(context, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(context, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(context, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    public void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }

    public void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void updateMetaData() {
        /*Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                R.drawable.image);*/ //replace with medias albumArt
        // Update the current metadata
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                //.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                // .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentTrack.getUser().getLogin())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "owner")
                // .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, currentTrack.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "track name")
                .build());
    }
}
