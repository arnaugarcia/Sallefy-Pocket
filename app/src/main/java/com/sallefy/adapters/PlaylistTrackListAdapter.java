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
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistTrackListAdapter
        extends RecyclerView.Adapter<PlaylistTrackListAdapter.ViewHolder> {

    private FragmentManager fragmentManager;
    private Context context;

    private Playlist playlist;

    private int selectedItem = RecyclerView.NO_POSITION;

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
            holder.makeOtherItems();
            holder.itemView.setOnClickListener(v -> {
                selectedItem = position;
                notifyItemRangeChanged(1, playlist.getTracks().size() + 1);
                System.out.println("Track index: " + (position - 1));
            });

            if (!playlist.getTracks().isEmpty()) {
                if (selectedItem == position) {
                    holder.showSelected();
                    setTextsSelected(holder, playlist.getTracks().get(position - 1));
                } else {
                    holder.showUnselected();
                    setTextsUnselected(holder, playlist.getTracks().get(position - 1));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return (playlist.getTracks().isEmpty()) ? 1 : playlist.getTracks().size() + 1;
    }

    private void setTextsUnselected(ViewHolder holder, Track track) {
        holder.tvTrackTitle.setText(track.getName());
        holder.tvOwner.setText(track.getUser().getLogin());
    }

    private void setTextsSelected(ViewHolder holder, Track track) {
        holder.tvSelectedTrackTitle.setText(track.getName());
        holder.tvSelectedTrackTitle.setSelected(true);
        holder.tvSelectedOwner.setText(track.getUser().getLogin());
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout playlistDataLayout;
        TextView tvPlaylistName;
        TextView tvPlaylistDescription;
        CircularImageView ivPlaylistThumbnail;


        ConstraintLayout trackItemLayout;
        ConstraintLayout unselectedLayout;
        TextView tvTrackTitle;
        TextView tvOwner;
        AppCompatImageButton ibFavourite;

        ConstraintLayout selectedLayout;
        ImageView ivSelectedThumbnail;
        AppCompatImageButton ibSelectedFavourite;
        TextView tvSelectedTrackTitle;
        TextView tvSelectedOwner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistDataLayout = itemView.findViewById(R.id.playlist_data);
            ivPlaylistThumbnail = itemView.findViewById(R.id.iv_playlist_thumbnail);
            tvPlaylistName = itemView.findViewById(R.id.tv_playlist_title);
            tvPlaylistDescription = itemView.findViewById(R.id.tv_playlist_description);

            trackItemLayout = itemView.findViewById(R.id.track_layout);
            unselectedLayout = itemView.findViewById(R.id.unselected_track);
            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvOwner = itemView.findViewById(R.id.tv_owner);
            ibFavourite = itemView.findViewById(R.id.ib_favourite);

            selectedLayout = itemView.findViewById(R.id.selected_track);
            ivSelectedThumbnail = itemView.findViewById(R.id.iv_selected_thumbnail);
            tvSelectedTrackTitle = itemView.findViewById(R.id.tv_selected_track_title);
            tvSelectedOwner = itemView.findViewById(R.id.tv_selected_owner);
            ibSelectedFavourite = itemView.findViewById(R.id.ib_selected_favourite);
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

        void makeOtherItems() {
            playlistDataLayout.setVisibility(View.GONE);
            trackItemLayout.setVisibility(View.VISIBLE);
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
