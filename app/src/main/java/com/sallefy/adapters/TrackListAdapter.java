package com.sallefy.adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sallefy.R;
import com.sallefy.fragments.AddToPlaylistFragment;
import com.sallefy.managers.tracks.TrackManager;
import com.sallefy.managers.tracks.UpdateTrackLikedCallback;
import com.sallefy.model.LikedDTO;
import com.sallefy.model.Track;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder>
        implements UpdateTrackLikedCallback {

    private Context mContext;
    private FragmentManager mFragmentManager;
    private List<Track> tracks;

    private AppCompatImageButton ibMore, ibSelectedMore;

    private int selectedItem = RecyclerView.NO_POSITION;

    public TrackListAdapter(Context context, List<Track> tracks, FragmentManager fragmentManager) {
        this.mContext = context;
        this.tracks = tracks;
        this.mFragmentManager = fragmentManager;
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
        Track currentTrack = tracks.get(position);

        holder.itemView.setOnClickListener(v -> {
            selectedItem = position;
            notifyItemRangeChanged(0, tracks.size());
        });

        if (selectedItem == position) {
            holder.showSelected();
            setTextsSelected(holder, currentTrack);
        } else {
            holder.showUnselected();
            setTextsUnselected(holder, currentTrack);
        }

        holder.itemView.setOnLongClickListener(v -> {
            processOptions(holder, currentTrack);
            return false;
        });

        ibMore.setOnClickListener(v -> processOptions(holder, currentTrack));
        ibSelectedMore.setOnClickListener(v -> processOptions(holder, currentTrack));
    }

    @Override
    public int getItemCount() {
        return (tracks != null) ? tracks.size() : 0;
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

    private void updateTrackLiked(String trackId){
        TrackManager.getInstance().updateTrackLiked(mContext, trackId, this);
    }

    @Override
    public void onMyTracksSuccess(LikedDTO liked) {
        if (liked.isLiked()){
            Toast.makeText(mContext, "Added to liked tracks", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Removed from liked tracks", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMyTracksFailure(Throwable throwable) {
        Toast.makeText(mContext, "Error: Unable to like track", Toast.LENGTH_SHORT).show();
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

    private void setTextsUnselected(ViewHolder holder, Track currentTrack) {
        holder.tvTrackTitle.setText(currentTrack.getName());
        holder.tvOwner.setText(currentTrack.getUser().getLogin());
    }

    private void setTextsSelected(ViewHolder holder, Track currentTrack) {
        holder.tvSelectedTrackTitle.setText(currentTrack.getName());
        holder.tvSelectedTrackTitle.setSelected(true);
        holder.tvSelectedOwner.setText(currentTrack.getUser().getLogin());
        if (currentTrack.getThumbnail() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.application_logo)
                    .load(currentTrack.getThumbnail())
                    .apply(RequestOptions.bitmapTransform(new GranularRoundedCorners(20,
                            0,
                            0,
                            20))
                    )
                    .into(holder.ivSelectedThumbnail);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout unselectedLayout;
        TextView tvTrackTitle;
        TextView tvOwner;

        ConstraintLayout selectedLayout;
        ImageView ivSelectedThumbnail;
        TextView tvSelectedTrackTitle;
        TextView tvSelectedOwner;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            unselectedLayout = itemView.findViewById(R.id.unselected_track);

            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvOwner = itemView.findViewById(R.id.tv_owner);
            ibMore = itemView.findViewById(R.id.ib_favourite);


            selectedLayout = itemView.findViewById(R.id.selected_track);

            ivSelectedThumbnail = itemView.findViewById(R.id.iv_selected_thumbnail);
            tvSelectedTrackTitle = itemView.findViewById(R.id.tv_selected_track_title);
            tvSelectedOwner = itemView.findViewById(R.id.tv_selected_owner);
            ibSelectedMore = itemView.findViewById(R.id.ib_selected_favourite);

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
