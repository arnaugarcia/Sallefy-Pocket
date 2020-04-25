package com.sallefy.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.sallefy.model.Track;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeaturedTrackListAdapter extends RecyclerView.Adapter<FeaturedTrackListAdapter.ViewHolder> {

    private Context context;

    private List<Track> tracks;

    public FeaturedTrackListAdapter(Context context, List<Track> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public FeaturedTrackListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedTrackListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
