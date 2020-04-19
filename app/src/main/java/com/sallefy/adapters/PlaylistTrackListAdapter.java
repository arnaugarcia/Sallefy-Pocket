package com.sallefy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sallefy.R;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistTrackListAdapter
        extends RecyclerView.Adapter<PlaylistTrackListAdapter.ViewHolder> {

    private FragmentManager fragmentManager;
    private Context context;

    private Playlist playlist;

    public PlaylistTrackListAdapter(FragmentManager fragmentManager, Context context, Playlist playlist) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.playlist = playlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_track_item, parent, false);
        return new PlaylistTrackListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.makeFirstItem();
        } else {
            holder.makeOtherItems(playlist.getTracks().get(position));
        }
    }

    @Override
    public int getItemCount() {
        return playlist.getTracks().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout playlistDataLayout;
        TextView tvPlaylistName;
        TextView tvPlaylistDescription;
        CircularImageView ivPlaylistThumbnail;

        ConstraintLayout trackItemLayout;
        ImageView ivTrackThumbnail;
        TextView tvTrackTitle;
        TextView tvTrackOwner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistDataLayout = itemView.findViewById(R.id.playlist_data);
            ivPlaylistThumbnail = itemView.findViewById(R.id.iv_playlist_thumbnail);
            tvPlaylistName = itemView.findViewById(R.id.tv_playlist_title);
            tvPlaylistDescription = itemView.findViewById(R.id.tv_playlist_description);

            trackItemLayout = itemView.findViewById(R.id.track_layout);
            ivTrackThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvTrackOwner = itemView.findViewById(R.id.tv_owner);
        }

        public void makeFirstItem() {
            playlistDataLayout.setVisibility(View.VISIBLE);
            trackItemLayout.setVisibility(View.GONE);

            tvPlaylistName.setText(playlist.getName());
            tvPlaylistDescription.setText(playlist.getDescription());
            if (playlist.getThumbnail() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.application_logo)
                        .load(playlist.getThumbnail())
                        .into(ivPlaylistThumbnail);
            }
        }

        public void makeOtherItems(Track track) {
            playlistDataLayout.setVisibility(View.GONE);
            trackItemLayout.setVisibility(View.VISIBLE);

            tvTrackTitle.setText(track.getName());
            tvTrackOwner.setText(track.getUserLogin());
            if (track.getThumbnail() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.application_logo)
                        .load(track.getThumbnail())
                        .into(ivTrackThumbnail);
            }
        }
    }
}
