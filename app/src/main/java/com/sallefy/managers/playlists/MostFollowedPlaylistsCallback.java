package com.sallefy.managers.playlists;

import com.sallefy.model.Playlist;

import java.util.List;

public interface MostFollowedPlaylistsCallback {
    void onMostFollowedPlaylistsSuccess(List<Playlist> playlists);
    void onMostFollowedPlaylistsFailure(Throwable throwable);
}
