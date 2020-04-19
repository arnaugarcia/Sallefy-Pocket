package com.sallefy.fragments;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sallefy.R;

public class SearchFragment extends Fragment {
    private TextView mSearchTitleTextView;
    private SearchView mSearchView;
    private RecyclerView mGenreRecyclerView;

    public SearchFragment() {
    }

    public static SearchFragment getInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews(View view) {
        mSearchTitleTextView = view.findViewById(R.id.tv_search_title);
        Shader shader = new LinearGradient(0,0,0, mSearchTitleTextView.getLineHeight(),
                ContextCompat.getColor(view.getContext(), R.color.gradientStart),
                ContextCompat.getColor(view.getContext(), R.color.gradientEnd),
                Shader.TileMode.MIRROR);
        mSearchTitleTextView.getPaint().setShader(shader);

        mGenreRecyclerView = view.findViewById(R.id.genre_recycle_view);



    }
}
