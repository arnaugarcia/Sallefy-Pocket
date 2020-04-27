package com.sallefy.managers.playlists;

import com.sallefy.model.Playlist;

public interface UpdatePlaylistCallback {
    void onUpdatePlaylistSuccess(Playlist playlist);
    void onUpdatePlaylistFailure(Throwable throwable);
}
