package com.sallefy.managers.user;

import com.sallefy.model.Playlist;

import java.util.List;

public interface PlaylistsByLoginCallback {
    void onUserPlaylistsReceived(List<Playlist> playlists);
    void onUserPlaylistsFailure(Throwable throwable);
}
