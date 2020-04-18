package com.sallefy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sallefy.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class YourLibraryAdapter extends RecyclerView.Adapter<YourLibraryAdapter.ViewHolder> {

    public static int PLAYLISTS = 0;
    public static int TRACKS = 1;

    private Context context;

    private List<String> types;
    private int listType;

    public YourLibraryAdapter(Context context, int listType) {
        this.context = context;
        this.listType = listType;
        this.types = new ArrayList<>();
        this.types.add("Playlist");
        this.types.add("Tracks");
    }

    @NonNull
    @Override
    public YourLibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_library_item, parent, false);
        return new YourLibraryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YourLibraryAdapter.ViewHolder holder, int position) {
        holder.tv.setText(types.get(position));
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.tv);
        }
    }
}
