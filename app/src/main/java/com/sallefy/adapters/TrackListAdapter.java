package com.sallefy.adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sallefy.R;
import com.sallefy.adapters.callbacks.TrackListCallback;
import com.sallefy.fragments.AddToPlaylistFragment;
import com.sallefy.managers.tracks.TrackManager;
import com.sallefy.managers.tracks.UpdateTrackLikedCallback;
import com.sallefy.model.LikedDTO;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> implements UpdateTrackLikedCallback {

    public static int TRACK_LIST = 0;
    public static int PLAYLIST_TRACK_LIST = 1;

    private FragmentManager mFragmentManager;
    private Context mContext;
    private List<Track> tracks;

    private Playlist playlist;
    private TrackListCallback trackCallback;
    private int type;

    private AppCompatImageButton ibMore, ibSelectedMore;

    private int selectedItem = RecyclerView.NO_POSITION;

    public TrackListAdapter(TrackListCallback trackCallback, Context mContext, List<Track> tracks) {
        this.mContext = mContext;
        this.tracks = tracks;
        this.trackCallback = trackCallback;
        this.type = TRACK_LIST;
    }

    public TrackListAdapter(TrackListCallback trackCallback, Context mContext, Playlist playlist) {
        this.mContext = mContext;
        this.playlist = playlist;
        this.trackCallback = trackCallback;
        this.type = PLAYLIST_TRACK_LIST;
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

        if (type == TRACK_LIST) {

            Track currentTrack = tracks.get(position);

            holder.itemView.setOnClickListener(v -> {
                selectedItem = position;
                notifyItemRangeChanged(0, tracks.size());
            });

            if (selectedItem == position) {
                holder.showSelected();
                setValuesSelected(holder, currentTrack);
            } else {
                holder.showUnselected();
                setValuesUnselected(holder, currentTrack);
            }

            holder.itemView.setOnLongClickListener(v -> {
                processOptions(holder, currentTrack);
                return false;
            });

            ibMore.setOnClickListener(v -> processOptions(holder, currentTrack));
            ibSelectedMore.setOnClickListener(v -> processOptions(holder, currentTrack));

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
    }

    private void setValuesSelected(ViewHolder holder, Track track) {
        holder.tvSelectedTrackTitle.setText(track.getName());
        holder.tvSelectedOwner.setText(track.getUser().getLogin());
        if (track.getThumbnail() != null) {
            Glide.with(mContext)
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

    private void processOptions(ViewHolder holder, Track currentTrack){
        PopupMenu popupMenu = new PopupMenu(mContext, holder.itemView);
        popupMenu.inflate(R.menu.menu_track_options);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.setGravity(Gravity.END);
        //TODO: Refresh state of like button on click
/*        MenuItem like = popupMenu.getMenu().findItem(R.id.menu_track_like);

        if (currentTrack.isLiked()) {
            like.setIcon(R.drawable.outline_favorite_24);
        } else {
            like.setIcon(R.drawable.outline_favorite_border_24);
        }*/
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_track_like:
                    Toast.makeText(mContext, currentTrack.getName(), Toast.LENGTH_SHORT).show();
                    updateTrackLiked(currentTrack.getId().toString());
                    break;
                case R.id.menu_track_add_to_playlist:
                    openAddToPlaylistFragment(currentTrack);
                    break;
                case R.id.menu_track_share:
                    Toast.makeText(mContext, "share!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_track_owner:
                    Toast.makeText(mContext, "owner!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    return false;
            }
            return false;
        });

        popupMenu.show();
    }

    private void openAddToPlaylistFragment(Track currentTrack){
        AddToPlaylistFragment addToPlaylistFragment = new AddToPlaylistFragment(mContext, mFragmentManager);
        Bundle bundle = new Bundle();
        bundle.putSerializable("track", currentTrack);
        addToPlaylistFragment.setArguments(bundle);
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_manager, addToPlaylistFragment, "addToPlaylistFragment")
                .addToBackStack(null)
                .commit();
    }

    private void updateTrackLiked(String trackId){
        TrackManager.getInstance().updateTrackLiked(mContext, trackId, this);
    }

    @Override
    public void onMyTracksSuccess(LikedDTO liked) {

    }

    @Override
    public void onMyTracksFailure(Throwable throwable) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout unselectedLayout;
        TextView tvTrackTitle;
        TextView tvOwner;

        ConstraintLayout selectedLayout;
        ImageView ivSelectedThumbnail;
        TextView tvSelectedTrackTitle;
        TextView tvSelectedOwner;

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
            ibMore = itemView.findViewById(R.id.ib_favourite);


            selectedLayout = itemView.findViewById(R.id.selected_track);

            playlistDataLayout = itemView.findViewById(R.id.playlist_data);

            ivSelectedThumbnail = itemView.findViewById(R.id.iv_selected_thumbnail);
            tvSelectedTrackTitle = itemView.findViewById(R.id.tv_selected_track_title);
            tvSelectedOwner = itemView.findViewById(R.id.tv_selected_owner);
            ibSelectedMore = itemView.findViewById(R.id.ib_selected_favourite);
        }

        void makePlaylistDataItem(Playlist playlist) {
            playlistDataLayout.setVisibility(View.VISIBLE);
            unselectedLayout.setVisibility(View.GONE);
            selectedLayout.setVisibility(View.GONE);

            tvPlaylistTitle.setText(playlist.getName());
            tvPlaylistDescription.setText(playlist.getDescription());
            if (playlist.getThumbnail() != null) {
                Glide.with(mContext)
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
