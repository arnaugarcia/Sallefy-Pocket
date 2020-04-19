package com.sallefy.adapters.callbacks;

import com.sallefy.model.Track;

public interface TrackListCallback {
    void onTrackSelected(Track track);
    void onTrackLiked(Track track);
}
