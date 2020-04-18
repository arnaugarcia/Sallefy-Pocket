package com.sallefy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sallefy.R;
import com.sallefy.model.Playlist;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.ViewHolder> {

    private Context context;
    private List<Playlist> playlists;

    public PlaylistListAdapter(Context context, List<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item, parent, false);
        return new PlaylistListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        holder.tvPlaylistTitle.setText(playlist.getName());
        int numSongs = playlist.getTracks().size();
        holder.tvNumSongs.setText((numSongs == 1) ? numSongs + " song" : numSongs + " songs");

        if (playlist.getThumbnail() != null) {
            Glide.with(context)
                    .asBitmap()
                    .placeholder(R.drawable.application_logo)
                    .load(playlist.getThumbnail())
                    .into(holder.ivThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        TextView tvPlaylistTitle;
        TextView tvNumSongs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvPlaylistTitle = itemView.findViewById(R.id.tv_title);
            tvNumSongs = itemView.findViewById(R.id.tv_songs_number);
        }
    }
}