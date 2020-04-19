package com.sallefy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sallefy.R;
import com.sallefy.activity.CreatePlaylistActivity;
import com.sallefy.fragments.PlaylistFragment;
import com.sallefy.model.Playlist;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.ViewHolder> {

    private FragmentManager fragmentManager;
    private Context context;

    private List<Playlist> playlists;

    public PlaylistListAdapter(Context context, List<Playlist> playlists, FragmentManager fragmentManager) {
        this.context = context;
        this.playlists = playlists;
        this.fragmentManager = fragmentManager;
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

        if (position == 0) {
            holder.makeFirstItem();
        } else {
            holder.makeOtherItems(position);
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
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout createPlaylistLayout;
        ConstraintLayout playlistLayout;

        ImageView ivThumbnail;
        TextView tvPlaylistTitle;
        TextView tvNumSongs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            createPlaylistLayout = itemView.findViewById(R.id.create_layout);
            playlistLayout = itemView.findViewById(R.id.playlist_layout);

            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvPlaylistTitle = itemView.findViewById(R.id.tv_title);
            tvNumSongs = itemView.findViewById(R.id.tv_songs_number);
        }

        public void makeFirstItem() {
            createPlaylistLayout.setVisibility(VISIBLE);
            playlistLayout.setVisibility(GONE);

            createPlaylistLayout.setOnClickListener(layoutView -> {
                Intent intent = new Intent(context, CreatePlaylistActivity.class);
                context.startActivity(intent);
            });
        }

        public void makeOtherItems(int position) {
            createPlaylistLayout.setVisibility(GONE);
            playlistLayout.setVisibility(VISIBLE);

            playlistLayout.setOnClickListener(layoutView -> fragmentManager.beginTransaction()
                    .replace(R.id.fragment_manager, new PlaylistFragment(context, playlists.get(position), fragmentManager))
                    .addToBackStack(null)
                    .commit());
        }
    }
}
