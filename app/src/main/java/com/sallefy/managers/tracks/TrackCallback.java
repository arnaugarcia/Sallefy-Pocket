package com.sallefy.managers.tracks;


import com.sallefy.model.Track;

public interface TrackCallback {
    void onTrackSuccess(Track track);

    void onTrackFailure(Throwable throwable);
}
