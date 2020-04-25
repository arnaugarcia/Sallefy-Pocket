package com.sallefy.managers.tracks;

import com.sallefy.model.Track;

import java.util.List;

public interface MostPlayedTracksCallback {
    void onMostPlayedTracksSuccess(List<Track> tracks);
    void onMostPlayedTracksFailure(Throwable throwable);
}
