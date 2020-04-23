package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sallefy.R;
import com.sallefy.adapters.TrackListAdapter;
import com.sallefy.managers.search.SearchResponseCallback;
import com.sallefy.managers.search.SearchResponseManager;
import com.sallefy.model.SearchResult;

public class SearchResponseFragment extends Fragment implements SearchResponseCallback {

    private static SearchFragment instance;

    private Context mContext;
    private SearchResult mSearchResult;
    private FragmentManager mFragmentManager;

    private SearchView mSearchView;
    private TrackListAdapter mTrackListAdapter;
    private ImageButton mImageButtonBack;
    private RecyclerView mTrackRecyclerView;

    public SearchResponseFragment(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_response, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show();
                getTracksBySearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mImageButtonBack.setOnClickListener(v -> mFragmentManager.popBackStack());

        LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mTrackRecyclerView.setLayoutManager(manager);
        mTrackListAdapter = new TrackListAdapter(mContext, null);

    }

    private void initViews(View view) {
        mSearchView = view.findViewById(R.id.search_view);
        mSearchView.setIconified(false);
        mSearchView.clearFocus();

        mImageButtonBack = view.findViewById(R.id.ib_search_response_back);
        mTrackRecyclerView = view.findViewById(R.id.rv_search_response_tracks);

    }

    private void getTracksBySearchQuery(String keyword){
        SearchResponseManager.getInstance().getPlaylistsUsersTracksByKeyword(mContext, keyword, this);
    }

    @Override
    public void onSearchResponseReceived(SearchResult result) {
        mTrackListAdapter.setTracks(result.getTracks());
        mTrackRecyclerView.setAdapter(mTrackListAdapter);
    }

    @Override
    public void onSearchResponseFailure(Throwable throwable) {
        Toast.makeText(mContext, "Error receiving search result", Toast.LENGTH_SHORT).show();
    }
}
