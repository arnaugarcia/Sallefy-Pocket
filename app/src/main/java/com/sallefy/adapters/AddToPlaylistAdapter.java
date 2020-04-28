package com.sallefy.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.sallefy.activity.CreatePlaylistActivity;
import com.sallefy.constants.ApplicationConstants;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.managers.playlists.UpdatePlaylistCallback;
import com.sallefy.model.Playlist;
import com.sallefy.model.PlaylistRequest;
import com.sallefy.model.Track;

import java.util.List;
import java.util.Optional;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AddToPlaylistAdapter extends RecyclerView.Adapter<AddToPlaylistAdapter.ViewHolder>
        implements UpdatePlaylistCallback {

    private FragmentManager mFragmentManager;
    private Context mContext;
    private List<Playlist> mPlaylists;
    private Track mTrackToAdd;

    public AddToPlaylistAdapter(Context context, FragmentManager fragmentManager, List<Playlist> playlists, Track track) {
        this.mContext = context;
        this.mPlaylists = playlists;
        this.mFragmentManager = fragmentManager;
        this.mTrackToAdd = track;
    }

    public void setPlaylists(List<Playlist> mPlaylists) {
        this.mPlaylists = mPlaylists;
    }

    @NonNull
    @Override
    public AddToPlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item, parent, false);
        return new AddToPlaylistAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddToPlaylistAdapter.ViewHolder holder, int position) {
        if (position == 0) {
            holder.makeFirstItem(mTrackToAdd);
        } else {
            holder.itemView.setOnClickListener(v -> {
                PlaylistRequest playlistRequest = new PlaylistRequest(mPlaylists.get(position -1 ));
                playlistRequest.addTrack(mTrackToAdd);
                updatePlaylist(playlistRequest);
                mFragmentManager.popBackStack();
            });
            holder.makeOtherItems();
            Playlist playlist = mPlaylists.get(position - 1);

            holder.tvPlaylistTitle.setText(playlist.getName());
            int numSongs = playlist.getTracks().size();
            holder.tvNumSongs.setText((numSongs == 1) ? numSongs + " song" : numSongs + " songs");

            if (playlist.getThumbnail() != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.application_logo)
                        .load(playlist.getThumbnail())
                        .into(holder.ivThumbnail);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mPlaylists.size() + 1;
    }

    private void updatePlaylist(PlaylistRequest playlistRequest){
        PlaylistManager.getInstance().updatePlaylist(mContext, playlistRequest, this);
    }

    @Override
    public void onUpdatePlaylistSuccess(Playlist playlist) {
        Toast.makeText(mContext, "Added to " + playlist.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdatePlaylistFailure(Throwable throwable) {
        Log.d(ApplicationConstants.LOGCAT_ID, throwable.getMessage());
        Toast.makeText(mContext, "Error: unable to add track to playlist", Toast.LENGTH_SHORT).show();
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

        public void makeFirstItem(Track track) {
            createPlaylistLayout.setVisibility(VISIBLE);
            playlistLayout.setVisibility(GONE);

            createPlaylistLayout.setOnClickListener(layoutView -> {
                Intent intent = new Intent(mContext, CreatePlaylistActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (track != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("track", track);
                    intent.putExtras(bundle);
                }
                mContext.startActivity(intent);
                mFragmentManager.popBackStack();
            });
        }

        public void makeOtherItems() {
            createPlaylistLayout.setVisibility(GONE);
            playlistLayout.setVisibility(VISIBLE);
        }
    }
}
