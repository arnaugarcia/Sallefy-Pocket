package com.sallefy.managers.playlists;

import com.sallefy.model.Playlist;

public interface CreatePlaylistCallback {
    void onCreatePlaylistSuccess(Playlist playlist);
    void onCreatePlaylistFailure(Throwable throwable);
}
