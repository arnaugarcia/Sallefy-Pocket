package com.sallefy.managers.playlists;

import com.sallefy.model.LikedDTO;

public interface FollowPlaylistCallback {
    void onFollowPlaylistSuccess(LikedDTO following);
    void onFollowPlaylistFailure(Throwable throwable);
}
