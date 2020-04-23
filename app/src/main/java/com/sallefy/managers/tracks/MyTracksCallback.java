package com.sallefy.managers.tracks;

import com.sallefy.model.Track;

import java.util.List;

public interface MyTracksCallback {
    void onMyTracksReceived(List<Track> tracks);
    void onMyTracksFailure(Throwable throwable);
}
