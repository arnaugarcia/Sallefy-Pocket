package com.sallefy.managers.user;

import com.sallefy.model.Track;

import java.util.List;

public interface TracksByLoginCallback {
    void onUserTracksReceived(List<Track> tracks);
    void onUserTracksFailure(Throwable throwable);
}
