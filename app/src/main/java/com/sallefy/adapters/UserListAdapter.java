package com.sallefy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textview.MaterialTextView;
import com.sallefy.R;
import com.sallefy.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context context;

    private List<User> users;

    public UserListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvLogin.setText(user.getLogin());
        holder.tvFollowers.setText(user.getFollowers());
        Glide.with(context)
                .asBitmap()
                .apply(RequestOptions.circleCropTransform())
                .load(user.getImageUrl())
                .into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return (users != null) ? users.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        MaterialTextView tvLogin, tvFollowers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initViews(itemView);
        }

        void initViews(View itemView) {
            ivThumbnail = itemView.findViewById(R.id.iv_user_thumbnail);
            tvLogin = itemView.findViewById(R.id.tv_user_name);
            tvFollowers = itemView.findViewById(R.id.tv_user_followers);
        }
    }
}
