package com.sallefy.managers.genres;

import com.sallefy.model.Genre;

import java.util.List;

public interface GenresCallback {
    void onGetAllGenresReceived(List<Genre> genres);
    void onGetAllGenresFailure(Throwable throwable);
}
