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
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sallefy.R;
import com.sallefy.adapters.SearchResponseAdapter;
import com.sallefy.adapters.callbacks.TrackListCallback;
import com.sallefy.managers.search.SearchResponseCallback;
import com.sallefy.managers.search.SearchResponseManager;
import com.sallefy.model.SearchResult;
import com.sallefy.model.Track;

import static com.sallefy.constants.ApplicationConstants.TAB_TITLES;

public class SearchResponseFragment extends Fragment implements SearchResponseCallback, TrackListCallback {

    //private static SearchResponseFragment instance;

    private Context mContext;
    private SearchResult mSearchResult;
    private FragmentManager mFragmentManager;

    private SearchView mSearchView;
    private SearchResponseAdapter mResponseAdapter;
    private ImageButton mImageButtonBack;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;



    public SearchResponseFragment(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.mResponseAdapter = new SearchResponseAdapter(this, context, fragmentManager);
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
                getTracksBySearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // No queremos saturar el backend
            }
        });

        mImageButtonBack.setOnClickListener(v -> mFragmentManager.popBackStack());

        mViewPager.setAdapter(mResponseAdapter);
        int colorAccent = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
        int colorTextPrimaryVariant = ResourcesCompat.getColor(getResources(), R.color.colorTextPrimaryVariant, null);
        mTabLayout.setSelectedTabIndicatorColor(colorAccent);
        mTabLayout.setTabTextColors(colorTextPrimaryVariant, colorAccent);

        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            tab.setText(TAB_TITLES[position]);
            mViewPager.setCurrentItem(tab.getPosition(), true);
        }).attach();
    }

    private void initViews(View view) {
        mSearchView = view.findViewById(R.id.search_view);
        mSearchView.setIconified(false);

        mImageButtonBack = view.findViewById(R.id.ib_search_response_back);

        mTabLayout = view.findViewById(R.id.search_tab_layout);
        mViewPager = view.findViewById(R.id.search_view_pager);

    }

    private void getTracksBySearchQuery(String keyword){
        SearchResponseManager.getInstance().getPlaylistsUsersTracksByKeyword(mContext, keyword, this);
    }

    @Override
    public void onSearchResponseReceived(SearchResult result) {
        mResponseAdapter.setmPlaylists(result.getPlaylists());
        mResponseAdapter.setmTracks(result.getTracks());
        mResponseAdapter.setmUsers(result.getUsers());
        mViewPager.setAdapter(mResponseAdapter);
    }

    @Override
    public void onSearchResponseFailure(Throwable throwable) {
        Toast.makeText(mContext, "Error receiving search result", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackLiked(Track track) {

    }
}
