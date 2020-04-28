package com.sallefy.managers.tracks;


import com.sallefy.model.LikedDTO;

import java.util.List;

import okhttp3.ResponseBody;

public interface UpdateTrackLikedCallback {
        void onMyTracksSuccess(LikedDTO liked);
        void onMyTracksFailure(Throwable throwable);
}
