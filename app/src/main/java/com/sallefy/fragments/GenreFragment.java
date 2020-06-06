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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.sallefy.R;
import com.sallefy.adapters.TrackListAdapter;
import com.sallefy.adapters.callbacks.TrackListCallback;
import com.sallefy.managers.tracks.TrackManager;
import com.sallefy.managers.tracks.TracksByGenreCallback;
import com.sallefy.model.Genre;
import com.sallefy.model.Track;

import java.util.List;


public class GenreFragment extends Fragment implements TracksByGenreCallback, TrackListCallback {

    private FragmentManager mFragmentManager;
    private Context context;

    private Genre genre;

    private ImageButton ibBack;
    private RecyclerView rvSongs;
    private MaterialTextView genreTitle;

    private TrackListAdapter trackListAdapter;

    private List<Track> tracks;

    public GenreFragment() {
    }

    public GenreFragment(FragmentManager mFragmentManager, Context context, Genre genre) {
        this.mFragmentManager = mFragmentManager;
        this.context = context;
        this.genre = genre;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        ibBack.setOnClickListener(v -> mFragmentManager.popBackStack());

        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rvSongs.setLayoutManager(manager);

        trackListAdapter = new TrackListAdapter(this, context, tracks, mFragmentManager);

        getTracksByGenre(genre.getName());
        genreTitle.setText(genre.getName());
    }

    private void initViews(View view) {
        ibBack = view.findViewById(R.id.genre_ib_back);
        rvSongs = view.findViewById(R.id.genre_rv_songs);
        genreTitle = view.findViewById(R.id.tv_genre_title);
    }

    private void getTracksByGenre(String genre) {
        TrackManager.getInstance().getTracksByGenre(genre, this);
    }

    @Override
    public void onTracksByGenreReceived(List<Track> tracks) {
        this.tracks = tracks;
        trackListAdapter.setTracks(tracks);
        rvSongs.setAdapter(trackListAdapter);
        //Log.d(ApplicationConstants.LOGCAT_ID, "Tracks: " + tracks);
    }

    @Override
    public void onTracksByGenreFailure(Throwable throwable) {
        Toast.makeText(context, "Error: No tracks received", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackLiked(Track track) {

    }
}
