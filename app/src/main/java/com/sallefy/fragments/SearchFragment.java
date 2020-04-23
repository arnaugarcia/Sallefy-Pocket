package com.sallefy.fragments;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sallefy.R;
import com.sallefy.adapters.GenreListAdapter;
import com.sallefy.managers.genres.GenreManager;
import com.sallefy.managers.genres.GenresCallback;
import com.sallefy.model.Genre;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment implements GenresCallback {

    private static SearchFragment instance;

    private Context mContext;
    private FragmentManager mFragmentManager;
    private Button mSearchBarButton;
    private TextView mSearchTitleTextView;
    private RecyclerView mGenreRecyclerView;
    private GenreListAdapter mGenreListAdapter;

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

        TextPaint paint = mSearchTitleTextView.getPaint();
        float width = paint.measureText(mSearchTitleTextView.getText().toString());
        Shader shader = new LinearGradient(0, 0, width, 0,
                ContextCompat.getColor(view.getContext(), R.color.gradientStart),
                ContextCompat.getColor(view.getContext(), R.color.gradientEnd),
                Shader.TileMode.MIRROR);
        mSearchTitleTextView.getPaint().setShader(shader);

        mSearchBarButton.setOnClickListener(v -> openSearchResponseFragment());

        mGenreListAdapter = new GenreListAdapter(getContext(), null, mFragmentManager);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mGenreRecyclerView.setLayoutManager(manager);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllGenres();
    }

    private void initViews(View view) {
        mSearchTitleTextView = view.findViewById(R.id.tv_search_title);
        mSearchBarButton = view.findViewById(R.id.b_search_search_bar);
        mGenreRecyclerView = view.findViewById(R.id.genre_recycle_view);
    }

    private void openSearchResponseFragment(){
        SearchResponseFragment searchResponseFragment = new SearchResponseFragment(mContext, mFragmentManager);
        mFragmentManager.beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), searchResponseFragment, "searchResponseFragment")
                .addToBackStack(null)
                .commit();
    }

    private void getAllGenres() {
        GenreManager.getInstance().getAllGenres(mContext, this);
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
}
