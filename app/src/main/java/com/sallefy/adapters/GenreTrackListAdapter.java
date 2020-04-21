package com.sallefy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sallefy.R;
import com.sallefy.model.Genre;
import com.sallefy.model.Track;

import java.util.List;

public class GenreTrackListAdapter extends RecyclerView.Adapter<GenreTrackListAdapter.ViewHolder> {
    private FragmentManager fragmentManager;
    private Context context;
    private Genre genre;
    private List<Track> trackList;

    public GenreTrackListAdapter(FragmentManager fragmentManager, Context context, Genre genre) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.genre = genre;
    }

    @NonNull
    @Override
    public GenreTrackListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_track_item, parent, false);
        return new GenreTrackListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreTrackListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (trackList != null) ? trackList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
