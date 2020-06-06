package com.sallefy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sallefy.R;
import com.sallefy.model.Track;

import java.util.List;

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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.featured_track_item, parent, false);
        return new FeaturedTrackListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedTrackListAdapter.ViewHolder holder, int position) {
        Track track = tracks.get(position);

        holder.tvTrackTitle.setText(track.getName());
        Glide.with(context)
                .asBitmap()
                .centerCrop()
                .override(130, 130)
                .placeholder(R.drawable.application_logo)
                .load(track.getThumbnail())
                .into(holder.ivTrackThumbnail);
    }

    @Override
    public int getItemCount() {
        return (tracks != null) ? tracks.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivTrackThumbnail;
        TextView tvTrackTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivTrackThumbnail = itemView.findViewById(R.id.iv_track_thumbnail);
            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
        }
    }
}
