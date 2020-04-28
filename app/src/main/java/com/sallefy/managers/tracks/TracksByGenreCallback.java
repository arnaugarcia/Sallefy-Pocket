package com.sallefy.managers.tracks;

import com.sallefy.model.Track;

import java.util.List;

public interface TracksByGenreCallback {
    void onTracksByGenreReceived(List<Track> tracks);
    void onTracksByGenreFailure(Throwable throwable);
}
