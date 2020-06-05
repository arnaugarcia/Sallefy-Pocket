package com.sallefy.managers.playlists;

import com.sallefy.model.Playlist;

import java.util.List;

public interface FollowedPlaylistsCallback {
    void onGetPlaylistsSuccess(List<Playlist> playlistList);
    void onGetPlaylistsFailure(Throwable throwable);

}
