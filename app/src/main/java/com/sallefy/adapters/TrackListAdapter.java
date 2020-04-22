package com.sallefy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sallefy.R;
import com.sallefy.model.Track;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    private Context context;
    private List<Track> tracks;

    private int selectedItem = RecyclerView.NO_POSITION;

    public TrackListAdapter(Context context, List<Track> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_item, parent, false);
        return new TrackListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = tracks.get(position);

        holder.itemView.setOnClickListener(v -> {
            selectedItem = position;
            notifyItemRangeChanged(0, tracks.size());
        });

        if (selectedItem == position) {
            holder.showSelected();
            setTextsSelected(holder, track);
        } else {
            holder.showUnselected();
            setTextsUnselected(holder, track);
        }
    }

    @Override
    public int getItemCount() {
        return (tracks != null) ? tracks.size() : 0;
    }

    private void setTextsUnselected(ViewHolder holder, Track track) {
        holder.tvTrackTitle.setText(track.getName());
        holder.tvOwner.setText(track.getUser().getLogin());
        //holder.tvDuration.setText(String.valueOf(track.getDuration()));
    }

    private void setTextsSelected(ViewHolder holder, Track track) {
        holder.tvSelectedTrackTitle.setText(track.getName());
        holder.tvSelectedTrackTitle.setSelected(true);
        holder.tvSelectedOwner.setText(track.getUser().getLogin());
        //holder.tvSelectedDuration.setText(String.valueOf(track.getDuration()));
        if (track.getThumbnail() != null) {
            Glide.with(context)
                    .asBitmap()
                    .placeholder(R.drawable.application_logo)
                    .load(track.getThumbnail())
                    .apply(RequestOptions.bitmapTransform(new GranularRoundedCorners(20,
                            0,
                            0,
                            20))
                    )
                    .into(holder.ivSelectedThumbnail);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout unselectedLayout;
        TextView tvTrackTitle;
        TextView tvOwner;
        //TextView tvDuration;
        AppCompatImageButton ibFavourite;

        ConstraintLayout selectedLayout;
        ImageView ivSelectedThumbnail;
        AppCompatImageButton ibSelectedFavourite;
        TextView tvSelectedTrackTitle;
        TextView tvSelectedOwner;
        TextView tvSelectedDuration;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            unselectedLayout = itemView.findViewById(R.id.unselected_track);

            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvOwner = itemView.findViewById(R.id.tv_owner);
            //tvDuration = itemView.findViewById(R.id.tv_duration);
            ibFavourite = itemView.findViewById(R.id.ib_favourite);


            selectedLayout = itemView.findViewById(R.id.selected_track);

            ivSelectedThumbnail = itemView.findViewById(R.id.iv_selected_thumbnail);
            tvSelectedTrackTitle = itemView.findViewById(R.id.tv_selected_track_title);
            tvSelectedOwner = itemView.findViewById(R.id.tv_selected_owner);
            //tvSelectedDuration = itemView.findViewById(R.id.tv_selected_duration);
            ibSelectedFavourite = itemView.findViewById(R.id.ib_selected_favourite);
        }

        void showSelected() {
            unselectedLayout.setVisibility(View.GONE);
            selectedLayout.setVisibility(View.VISIBLE);
        }

        void showUnselected() {
            unselectedLayout.setVisibility(View.VISIBLE);
            selectedLayout.setVisibility(View.GONE);
        }
    }
}
