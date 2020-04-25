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

public class FeaturedPlaylistListAdapter extends RecyclerView.Adapter<FeaturedPlaylistListAdapter.ViewHolder> {

    private Context context;

    private List<Playlist> playlists;

    public FeaturedPlaylistListAdapter(Context context, List<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.featured_playlist_item, parent, false);
        return new FeaturedPlaylistListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        holder.tvPlaylistTitle.setText(playlist.getName());
        holder.tvPlaylistOwnerLogin.setText(playlist.getUserLogin());
        holder.tvPlaylistTitle.setSelected(true);
        holder.tvPlaylistOwnerLogin.setSelected(true);
        Glide.with(context)
                .asBitmap()
                .placeholder(R.drawable.application_logo)
                .load(playlist.getThumbnail())
                .into(holder.ivPlaylistThumbnail);
    }

    @Override
    public int getItemCount() {
        return (playlists != null) ? playlists.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPlaylistThumbnail;
        TextView tvPlaylistTitle;
        TextView tvPlaylistOwnerLogin;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPlaylistThumbnail = itemView.findViewById(R.id.iv_playlist_thumbnail);
            tvPlaylistTitle = itemView.findViewById(R.id.tv_playlist_title);
            tvPlaylistOwnerLogin = itemView.findViewById(R.id.tv_playlist_owner);
        }
    }
}
