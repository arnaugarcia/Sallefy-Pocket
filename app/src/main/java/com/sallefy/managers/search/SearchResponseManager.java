package com.sallefy.managers.search;

import android.content.Context;
import android.util.Log;

import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.BaseManager;
import com.sallefy.model.SearchResult;
import com.sallefy.services.authentication.AuthenticationUtils;
import com.sallefy.services.search.SearchService;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResponseManager extends BaseManager {

    private static SearchResponseManager instance;
    private SearchService searchService;

    private SearchResponseManager() {
        searchService = retrofit.create(SearchService.class);
    }

    public static SearchResponseManager getInstance(){
        if (instance == null) instance = new SearchResponseManager();
        return instance;
    }

    public synchronized void getPlaylistsUsersTracksByKeyword(Context context, String keyword, final SearchResponseCallback responseCallback){
        Call<SearchResult> call = searchService.getPlaylistsUsersTracksByKeyword(keyword);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    responseCallback.onSearchResponseReceived((response.body()));
                } else {
                    Log.d(ApplicationConstants.LOGCAT_ID, "Error SearchResult not successful: " + response.toString());
                    responseCallback.onSearchResponseFailure(new Throwable(new Throwable("Error " + code + ", " + response.errorBody())));
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.d(ApplicationConstants.LOGCAT_ID, "Error SearchResult not successful: " + Arrays.toString(t.getStackTrace()));
                responseCallback.onSearchResponseFailure(new Throwable("Error " + Arrays.toString(t.getStackTrace())));
            }
        });

    }
}

