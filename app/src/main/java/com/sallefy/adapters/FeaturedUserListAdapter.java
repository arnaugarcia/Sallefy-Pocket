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
import com.sallefy.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeaturedUserListAdapter extends RecyclerView.Adapter<FeaturedUserListAdapter.ViewHolder> {

    private Context context;

    private List<User> users;

    public FeaturedUserListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.featured_user_item, parent, false);
        return new FeaturedUserListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvLogin.setText(user.getLogin());
        holder.tvLogin.setSelected(true);
        Glide.with(context)
                .asBitmap()
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.application_logo)
                .load(user.getImageUrl())
                .into(holder.ivPic);
    }

    @Override
    public int getItemCount() {
        return (users != null) ? users.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPic;
        TextView tvLogin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initViews(itemView);
        }

        void initViews(View itemView) {
            ivPic = itemView.findViewById(R.id.iv_user_pic);
            tvLogin = itemView.findViewById(R.id.tv_user_login);
        }
    }
}
