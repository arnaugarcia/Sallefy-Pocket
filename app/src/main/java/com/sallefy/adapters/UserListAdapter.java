package com.sallefy.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textview.MaterialTextView;
import com.sallefy.R;
import com.sallefy.fragments.OwnerFragment;
import com.sallefy.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private FragmentManager mFragmentManager;

    public UserListAdapter(Context context, List<User> users, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mUsers = users;
        this.mFragmentManager = fragmentManager;
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
        User user = mUsers.get(position);

        holder.tvLogin.setText(user.getLogin());
        holder.tvFollowers.setText(String.valueOf(user.getFollowers()));

        if(user.getImageUrl() != null){
            Glide.with(mContext)
                    .asBitmap()
                    .apply(RequestOptions.circleCropTransform())
                    .load(Uri.parse(user.getImageUrl()))
                    .into(holder.ivThumbnail);
        }else{
            Glide.with(mContext)
                    .asBitmap()
                    .apply(RequestOptions.circleCropTransform())
                    .load(R.drawable.account_circle)
                    .into(holder.ivThumbnail);
        }

        holder.itemView.setOnClickListener(v -> mFragmentManager.beginTransaction()
                .replace(R.id.fragment_manager, new OwnerFragment(mContext, mFragmentManager, mUsers.get(position)))
                .addToBackStack(null)
                .commit());
    }

    @Override
    public int getItemCount() {
        return (mUsers != null) ? mUsers.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        MaterialTextView tvLogin;
        MaterialTextView tvFollowers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initViews(itemView);
        }

        void initViews(View itemView) {
            ivThumbnail = itemView.findViewById(R.id.iv_user_thumbnail);
            tvLogin = itemView.findViewById(R.id.tv_user_name);
            tvFollowers = itemView.findViewById(R.id.tv_user_item_followers);
        }
    }
}
