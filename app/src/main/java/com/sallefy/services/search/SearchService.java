package com.sallefy.services.search;

import com.sallefy.model.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SearchService {
    @GET("search")
    Call<SearchResult> getPlaylistsUsersTracksByKeyword(@Query("keyword") String keyword);

}
