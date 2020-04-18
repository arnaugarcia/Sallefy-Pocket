package com.sallefy.managers.playlists;

import com.sallefy.model.Playlist;

import java.util.List;

public interface PlaylistCallback {
    void onMyPlaylistsReceived(List<Playlist> playlists);
    void onMyPlaylistsFailure(Throwable throwable);
}
