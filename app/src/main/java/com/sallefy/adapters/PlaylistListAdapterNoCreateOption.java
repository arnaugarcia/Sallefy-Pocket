package com.sallefy.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sallefy.R;
import com.sallefy.fragments.PlaylistFragment;
import com.sallefy.model.Playlist;

import java.util.List;

public class PlaylistListAdapterNoCreateOption extends RecyclerView.Adapter<PlaylistListAdapterNoCreateOption.ViewHolder> {

    private FragmentManager fragmentManager;
    private Context context;

    private List<Playlist> playlists;

    public PlaylistListAdapterNoCreateOption(Context context, List<Playlist> playlists, FragmentManager fragmentManager) {
        this.context = context;
        this.playlists = playlists;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item_no_create_option, parent, false);
        return new PlaylistListAdapterNoCreateOption.ViewHolder(itemView);
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
                    .load(Uri.parse(playlist.getThumbnail()))
                    .into(holder.ivThumbnail);
        }
        /*holder.playlistLayout.setOnClickListener(layoutView -> fragmentManager.beginTransaction()
                .replace(R.id.fragment_manager, new PlaylistFragment(context, playlist, fragmentManager))
                .addToBackStack(null)
                .commit());*/

        holder.itemView.setOnClickListener(v -> Toast.makeText(context, "HEY: "+playlist.getName(), Toast.LENGTH_SHORT).show());

    }

    @Override
    public int getItemCount() {
        return (playlists != null) ? playlists.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        TextView tvPlaylistTitle;
        TextView tvNumSongs;
        ConstraintLayout playlistLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistLayout = itemView.findViewById(R.id.playlist_layout_no_create_option);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail_no_create_option);
            tvPlaylistTitle = itemView.findViewById(R.id.tv_title_no_create_option);
            tvNumSongs = itemView.findViewById(R.id.tv_songs_number_no_create_option);
        }

        /*public void makeOtherItems(int position) {
            playlistLayout.setOnClickListener(layoutView -> fragmentManager.beginTransaction()
                    .replace(R.id.fragment_manager, new PlaylistFragment(context, playlists.get(position), fragmentManager))
                    .addToBackStack(null)
                    .commit());
        }*/
    }
}