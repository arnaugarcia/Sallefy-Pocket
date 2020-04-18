package com.sallefy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sallefy.R;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class YourLibraryAdapter extends RecyclerView.Adapter<YourLibraryAdapter.ViewHolder> {

    public static int PLAYLISTS = 0;
    public static int TRACKS = 1;

    private Context context;

    private List<Playlist> playlists;
    private List<Track> tracks;

    public YourLibraryAdapter(Context context) {
        this.context = context;
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
            if (this.playlists != null)
                adapter = new PlaylistListAdapter(context, this.playlists);
        } else if (position == 1) {
            if (this.tracks != null)
                adapter = new TrackListAdapter(context, this.tracks);
        }
        if (adapter != null) holder.rvYourLibrary.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rvYourLibrary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rvYourLibrary = itemView.findViewById(R.id.rv_your_library);
        }
    }
}
