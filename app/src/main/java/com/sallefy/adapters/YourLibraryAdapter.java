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

import java.util.List;

public class YourLibraryAdapter extends RecyclerView.Adapter<YourLibraryAdapter.ViewHolder> implements TrackListCallback {

    private FragmentManager mFragmentManager;
    private Context context;

    private List<Playlist> playlists;
    private List<Track> tracks;
    private TrackListCallback trackListCallback;

    public YourLibraryAdapter(TrackListCallback trackListCallback, Context context, FragmentManager mFragmentManager) {
        this.trackListCallback = trackListCallback;
        this.context = context;
        this.mFragmentManager = mFragmentManager;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public YourLibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_library_item, parent, false);
        return new YourLibraryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YourLibraryAdapter.ViewHolder holder, int position) {
        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        holder.rvYourLibrary.setLayoutManager(manager);

        RecyclerView.Adapter adapter = null;

        if (position == 0) {
            if (playlists != null)
                adapter = new PlaylistListAdapter(context, playlists, mFragmentManager);
        } else if (position == 1) {
            if (this.tracks != null)
                adapter = new TrackListAdapter(this, context, this.tracks, mFragmentManager);
        }
        if (adapter != null) holder.rvYourLibrary.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 2;
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
