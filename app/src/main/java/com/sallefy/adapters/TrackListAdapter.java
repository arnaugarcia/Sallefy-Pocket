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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sallefy.R;
import com.sallefy.adapters.callbacks.TrackListCallback;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    public static int TRACK_LIST = 0;
    public static int PLAYLIST_TRACK_LIST = 1;

    private FragmentManager fragmentManager;
    private Context context;

    private List<Track> tracks;
    private Playlist playlist;
    private TrackListCallback trackCallback;
    private int type;

    private int selectedItem = RecyclerView.NO_POSITION;

    public TrackListAdapter(TrackListCallback trackCallback, Context context, List<Track> tracks) {
        this.context = context;
        this.tracks = tracks;
        this.trackCallback = trackCallback;
        this.type = TRACK_LIST;
    }

    public TrackListAdapter(TrackListCallback trackCallback, Context context, Playlist playlist) {
        this.context = context;
        this.playlist = playlist;
        this.trackCallback = trackCallback;
        this.type = PLAYLIST_TRACK_LIST;
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
        if (type == TRACK_LIST) {
            Track track = tracks.get(position);

            holder.itemView.setOnClickListener(v -> {
                selectedItem = position;
                notifyItemRangeChanged(0, tracks.size());
            });

            if (selectedItem == position) {
                holder.showSelected();
                setValuesSelected(holder, track);
            } else {
                holder.showUnselected();
                setValuesUnselected(holder, track);
            }
        } else if (type == PLAYLIST_TRACK_LIST) {
            if (position == 0) {
                holder.makePlaylistDataItem(playlist);
            } else {

                holder.itemView.setOnClickListener(v -> {
                    selectedItem = position;
                    notifyItemRangeChanged(0, tracks.size());
                });

                boolean isSelected = selectedItem == position - 1;

                holder.makeTrackItems(isSelected);

                holder.itemView.setOnClickListener(v -> {
                    selectedItem = position;
                    notifyItemRangeChanged(1, playlist.getTracks().size() + 1);
                    System.out.println("Track index: " + (position - 1));
                });

                if (!playlist.getTracks().isEmpty()) {
                    if (selectedItem == position) {
                        holder.showSelected();
                        setValuesSelected(holder, playlist.getTracks().get(position - 1));
                    } else {
                        holder.showUnselected();
                        setValuesUnselected(holder, playlist.getTracks().get(position - 1));
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (type == TRACK_LIST) {
            return (tracks != null) ? tracks.size() : 0;
        } else if (type == PLAYLIST_TRACK_LIST) {
            return (playlist.getTracks().isEmpty()) ? 1 : playlist.getTracks().size() + 1;
        }

        return 0;
    }

    private void setValuesUnselected(ViewHolder holder, Track track) {
        holder.tvTrackTitle.setText(track.getName());
        holder.tvOwner.setText(track.getUser().getLogin());
        holder.tvDuration.setText(String.valueOf(track.getDuration()));
    }

    private void setValuesSelected(ViewHolder holder, Track track) {
        holder.tvSelectedTrackTitle.setText(track.getName());
        holder.tvSelectedOwner.setText(track.getUser().getLogin());
        holder.tvSelectedDuration.setText(String.valueOf(track.getDuration()));
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
        holder.itemView.setOnClickListener(listener -> trackCallback.onTrackSelected(track));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout unselectedLayout;
        TextView tvTrackTitle;
        TextView tvOwner;
        TextView tvDuration;
        AppCompatImageButton ibFavourite;

        ConstraintLayout selectedLayout;
        ImageView ivSelectedThumbnail;
        AppCompatImageButton ibSelectedFavourite;
        TextView tvSelectedTrackTitle;
        TextView tvSelectedOwner;
        TextView tvSelectedDuration;

        ConstraintLayout playlistDataLayout;
        TextView tvPlaylistTitle;
        TextView tvPlaylistDescription;
        CircularImageView ivPlaylistThumbnail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            initViews();
        }

        private void initViews() {
            unselectedLayout = itemView.findViewById(R.id.unselected_track);

            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvOwner = itemView.findViewById(R.id.tv_owner);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            ibFavourite = itemView.findViewById(R.id.ib_favourite);


            selectedLayout = itemView.findViewById(R.id.selected_track);

            ivSelectedThumbnail = itemView.findViewById(R.id.iv_selected_thumbnail);
            tvSelectedTrackTitle = itemView.findViewById(R.id.tv_selected_track_title);
            tvSelectedOwner = itemView.findViewById(R.id.tv_selected_owner);
            tvSelectedDuration = itemView.findViewById(R.id.tv_selected_duration);
            ibSelectedFavourite = itemView.findViewById(R.id.ib_selected_favourite);


            playlistDataLayout = itemView.findViewById(R.id.playlist_data);
            ivPlaylistThumbnail = itemView.findViewById(R.id.iv_playlist_thumbnail);
            tvPlaylistTitle = itemView.findViewById(R.id.tv_playlist_title);
            tvPlaylistDescription = itemView.findViewById(R.id.tv_playlist_description);
        }

        void makePlaylistDataItem(Playlist playlist) {
            playlistDataLayout.setVisibility(View.VISIBLE);
            unselectedLayout.setVisibility(View.GONE);
            selectedLayout.setVisibility(View.GONE);

            tvPlaylistTitle.setText(playlist.getName());
            tvPlaylistDescription.setText(playlist.getDescription());
            if (playlist.getThumbnail() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.application_logo)
                        .load(playlist.getThumbnail())
                        .into(ivPlaylistThumbnail);
            }
        }

        void makeTrackItems(boolean isSelected) {
            playlistDataLayout.setVisibility(View.GONE);
            if (isSelected) showSelected();
            else showUnselected();
        }

        void showSelected() {
            playlistDataLayout.setVisibility(View.GONE);
            unselectedLayout.setVisibility(View.GONE);
            selectedLayout.setVisibility(View.VISIBLE);
        }

        void showUnselected() {
            playlistDataLayout.setVisibility(View.GONE);
            unselectedLayout.setVisibility(View.VISIBLE);
            selectedLayout.setVisibility(View.GONE);
        }
    }
}
