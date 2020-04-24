package com.sallefy.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sallefy.R;
import com.sallefy.fragments.GenreFragment;
import com.sallefy.model.Genre;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> {

    private FragmentManager fragmentManager;
    private Context context;
    private List<Genre> genres;

    public GenreListAdapter(Context context, List<Genre> genres, FragmentManager fragmentManager) {
        this.context = context;
        this.genres = genres;
        this.fragmentManager = fragmentManager;
//        int[] genreColors = context.getResources().getIntArray(R.array.genreColors);
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

        //holder.drawable.setTint(color);

        holder.itemView.setOnClickListener(v -> fragmentManager.beginTransaction()
                .replace(R.id.fragment_manager,
                        new GenreFragment(fragmentManager, context, genres.get(position))
                )
                .addToBackStack(null)
                .commit());
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
