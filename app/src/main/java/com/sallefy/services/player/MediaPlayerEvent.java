package com.sallefy.services.player;

public class MediaPlayerEvent {

    public static class PlaybackDuration {

        public final int duration;

        public PlaybackDuration(int duration) {
            this.duration = duration;
        }
    }

    public static class PlaybackPosition {

        public final int position;

        public PlaybackPosition(int position) {
            this.position = position;
        }
    }

    public static class PlaybackCompleted {

    }

    public static class StateChanged {

        public final MediaPlayerState currentState;

        public StateChanged(MediaPlayerState currentState) {
            this.currentState = currentState;
        }
    }

}
