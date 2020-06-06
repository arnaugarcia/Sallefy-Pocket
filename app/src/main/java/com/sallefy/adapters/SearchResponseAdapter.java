package com.sallefy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sallefy.R;
import com.sallefy.adapters.callbacks.TrackListCallback;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;
import com.sallefy.model.User;

import java.util.List;

public class SearchResponseAdapter extends RecyclerView.Adapter<SearchResponseAdapter.ViewHolder> implements TrackListCallback {

    private FragmentManager mFragmentManager;
    private Context mContext;

    private List<Playlist> mPlaylists;
    private List<Track> mTracks;
    private List<User> mUsers;
    private TrackListCallback trackListCallback;

    public SearchResponseAdapter(TrackListCallback trackListCallback, Context context, FragmentManager mFragmentManager) {
        this.trackListCallback = trackListCallback;
        this.mContext = context;
        this.mFragmentManager = mFragmentManager;
    }

    public void setmPlaylists(List<Playlist> mPlaylists) {
        this.mPlaylists = mPlaylists;
    }

    public void setmTracks(List<Track> mTracks) {
        this.mTracks = mTracks;
    }

    public void setmUsers(List<User> mUsers) {
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public SearchResponseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_library_item, parent, false);
        return new SearchResponseAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResponseAdapter.ViewHolder holder, int position) {
        LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        holder.rvYourLibrary.setLayoutManager(manager);

        RecyclerView.Adapter adapter = null;

        if (position == 0) {
            if (this.mTracks != null)
                adapter = new TrackListAdapter(this, mContext, mTracks, mFragmentManager);
        } else if (position == 1) {
            if (mPlaylists != null)
                adapter = new PlaylistListAdapter(mContext, mPlaylists, mFragmentManager);
        } else if (position == 2){
            if (this.mUsers != null)
                adapter = new UserListAdapter(mContext, mUsers, mFragmentManager);
        }
        if (adapter != null) holder.rvYourLibrary.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
            return 3;
    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackLiked(Track track) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rvYourLibrary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rvYourLibrary = itemView.findViewById(R.id.rv_your_library);
        }
    }
}

