package com.sallefy.fragments;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sallefy.R;
import com.sallefy.adapters.GenreListAdapter;
import com.sallefy.adapters.TrackListAdapter;
import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.genres.GenreManager;
import com.sallefy.managers.genres.GenresCallback;
import com.sallefy.managers.search.SearchResponseCallback;
import com.sallefy.managers.search.SearchResponseManager;
import com.sallefy.model.Genre;
import com.sallefy.model.SearchResult;

import java.util.List;

public class SearchFragment extends Fragment implements GenresCallback, SearchResponseCallback {

    private static SearchFragment instance;

    private TextView mSearchTitleTextView;
    private Context mContext;
    private SearchView mSearchView;
    private RecyclerView mGenreRecyclerView;
    private FragmentManager mFragmentManager;
    private GenreListAdapter mGenreListAdapter;
    private TrackListAdapter mTrackListAdapter;

    public SearchFragment(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        getAllGenres();
    }

    public static SearchFragment getInstance(Context context, FragmentManager fragmentManager) {
        if (instance == null) instance = new SearchFragment(context, fragmentManager);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        mGenreListAdapter = new GenreListAdapter(getContext(), null, mFragmentManager);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mGenreRecyclerView.setLayoutManager(manager);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Toast.makeText(mContext, "SEARCH PAUSADO!", Toast.LENGTH_SHORT).show();
        mSearchView.clearFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllGenres();
    }

    private void initViews(View view) {
        mSearchTitleTextView = view.findViewById(R.id.tv_search_title);
        mSearchView = view.findViewById(R.id.search_view);
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
        TextPaint paint = mSearchTitleTextView.getPaint();
        float width = paint.measureText(mSearchTitleTextView.getText().toString());
        Shader shader = new LinearGradient(0, 0, width, 0,
                ContextCompat.getColor(view.getContext(), R.color.gradientStart),
                ContextCompat.getColor(view.getContext(), R.color.gradientEnd),
                Shader.TileMode.MIRROR);
        mSearchTitleTextView.getPaint().setShader(shader);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show();
                getTracksBySearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getTracksBySearchQuery(newText);
                return false;
            }
        });
        mGenreRecyclerView = view.findViewById(R.id.genre_recycle_view);
    }

    private void getAllGenres() {
        GenreManager.getInstance().getAllGenres(mContext, this);
    }

    private void getTracksBySearchQuery(String keyword){
        SearchResponseManager.getInstance().getPlaylistsUsersTracksByKeyword(mContext, keyword, this);
    }

    @Override
    public void onGetAllGenresReceived(List<Genre> genres) {
        mGenreListAdapter.setGenres(genres);
        mGenreRecyclerView.setAdapter(mGenreListAdapter);
    }

    @Override
    public void onGetAllGenresFailure(Throwable throwable) {
        Toast.makeText(mContext, "Error receiving genres", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchResponseReceived(SearchResult result) {
        //Toast.makeText(mContext, "RECEIVED!!", Toast.LENGTH_SHORT).show();
        Log.d(ApplicationConstants.LOGCAT_ID, result.getTracks().toString());

        mTrackListAdapter = new TrackListAdapter(getContext(), result.getTracks());
        LinearLayoutManager trackManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mGenreRecyclerView.setLayoutManager(trackManager);
        mGenreRecyclerView.setAdapter(mTrackListAdapter);
    }

    @Override
    public void onSearchResponseFailure(Throwable throwable) {
        Toast.makeText(mContext, "Error receiving searchResult", Toast.LENGTH_SHORT).show();
    }
}
