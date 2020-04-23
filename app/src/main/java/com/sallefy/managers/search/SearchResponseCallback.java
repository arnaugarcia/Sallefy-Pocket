package com.sallefy.managers.search;

import com.sallefy.model.SearchResult;

public interface SearchResponseCallback {
    void onSearchResponseReceived(SearchResult result);
    void onSearchResponseFailure(Throwable throwable);
}
