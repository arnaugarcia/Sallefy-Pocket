package com.sallefy.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sallefy.R;
import com.sallefy.model.Track;
import com.sallefy.services.player.PlayerService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import static android.widget.Toast.makeText;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    private Context context;
    private List<Track> tracks;
    // Service
    private PlayerService mBoundService;
    private boolean mServiceBound = false;

    public TrackListAdapter(Context context, List<Track> tracks) {
        this.context = context;
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

        holder.tvTrackTitle.setText(track.getName());
        holder.tvOwner.setText(track.getUser().getLogin());

        if (track.getThumbnail() != null) {
            Glide.with(context)
                    .asBitmap()
                    .placeholder(R.drawable.application_logo)
                    .load(track.getThumbnail())
                    .into(holder.ivThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        TextView tvTrackTitle;
        TextView tvOwner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvOwner = itemView.findViewById(R.id.tv_owner);

            itemView.setOnClickListener(playTrack());
        }
    }

    @NotNull
    private View.OnClickListener playTrack() {
        return v -> makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder)service;
            mBoundService = binder.getService();
            // mBoundService.setCallback(SongsFragment.this);
            mServiceBound = true;
            // updateSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };
}
