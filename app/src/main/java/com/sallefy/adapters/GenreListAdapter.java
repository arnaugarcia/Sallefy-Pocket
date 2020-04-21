package com.sallefy.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sallefy.R;
import com.sallefy.model.Genre;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> {

    private Context context;
    private List<Genre> genres;
    Queue<Integer> genreColorsQueue;

    public GenreListAdapter(Context context, List<Genre> genres) {
        this.context = context;
        this.genres = genres;
        int[] genreColors = context.getResources().getIntArray(R.array.genreColors);

        ArrayList<Integer> genreColorsList = new ArrayList<>();

        for (int i: genreColors) {
            genreColorsList.add(i);
        }

        genreColorsQueue = new LinkedList<>(genreColorsList);

    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_item, parent, false);
        return new GenreListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.tvGenre.setText(genre.getName());

        Integer currentColor = genreColorsQueue.remove();
        holder.drawable.setTint(currentColor);
        genreColorsQueue.offer(currentColor);

    }

    @Override
    public int getItemCount() {
        return (genres != null) ? genres.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenre;
        Drawable drawable;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenre = itemView.findViewById(R.id.tv_genre);
            drawable = itemView.getResources().getDrawable(R.drawable.round_empty_bg, null);
        }
    }
}
