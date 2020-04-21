package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sallefy.R;
import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.genres.GenreManager;
import com.sallefy.managers.tracks.TrackManager;
import com.sallefy.managers.tracks.TracksByGenreCallback;
import com.sallefy.model.Genre;
import com.sallefy.model.Track;

import java.util.List;


public class GenreFragment extends Fragment implements TracksByGenreCallback {

    private FragmentManager fragmentManager;
    private Context context;

    private Genre genre;

    private ImageButton ibBack;
    private RecyclerView rvSongs;

    private List<Track> tracks;

    public GenreFragment() {
    }

    public GenreFragment(FragmentManager fragmentManager, Context context, Genre genre) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.genre = genre;
        getTracksByGenre(genre.getName());
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

        ibBack.setOnClickListener(v -> fragmentManager.popBackStack());

        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rvSongs.setLayoutManager(manager);

    }

    private void initViews(View view) {
        ibBack = view.findViewById(R.id.genre_ib_back);
        rvSongs = view.findViewById(R.id.genre_rv_songs);
    }

    private void getTracksByGenre(String genre) {
        TrackManager.getInstance().getTracksByGenre(context, genre, this);
    }

    @Override
    public void onTracksByGenreReceived(List<Track> tracks) {
        this.tracks = tracks;
        Log.d(ApplicationConstants.LOGCAT_ID, "Tracks: " + tracks);
    }

    @Override
    public void onTracksByGenreFailure(Throwable throwable) {
        Toast.makeText(context, "Error: No tracks received", Toast.LENGTH_SHORT).show();
    }
}
