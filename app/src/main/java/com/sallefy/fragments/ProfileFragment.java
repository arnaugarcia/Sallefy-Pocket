package com.sallefy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sallefy.R;
import com.sallefy.activity.MainActivity;
import com.sallefy.managers.user.UserDataCallback;
import com.sallefy.managers.user.UserManager;
import com.sallefy.model.User;
import com.sallefy.services.authentication.AuthenticationUtils;

public class ProfileFragment extends Fragment implements UserDataCallback {

    private Context context;

    private TextView tvProfileTitle;
    private CircularImageView ivProfilePic;
    private TextView tvUsername;
    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvEmail;
    private TextView tvDateCreated;

    private ImageButton ibLogout;

    public ProfileFragment() {
    }

    public ProfileFragment(Context context) {
        this.context = context;
        getUserData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        ibLogout.setOnClickListener(v -> new MaterialAlertDialogBuilder(context, R.style.Dialog_Logout)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton("No, take me back", (dialog, i) -> Toast.makeText(context, "Not logging out", Toast.LENGTH_SHORT).show())
                .setPositiveButton("Yes, please", (dialog, i) -> {
                    logout();
                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
                })
                .show());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initViews(View view) {
        tvProfileTitle = view.findViewById(R.id.tv_profile_title);
        ivProfilePic = view.findViewById(R.id.iv_profile_pic);
        tvUsername = view.findViewById(R.id.tv_login);
        tvFirstName = view.findViewById(R.id.tv_first_name);
        tvLastName = view.findViewById(R.id.tv_last_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvDateCreated = view.findViewById(R.id.tv_date_created);
        ibLogout = view.findViewById(R.id.ib_logout);
    }

    private void getUserData() {
        UserManager.getInstance().getUserData(context, this);
    }

    private void logout() {
        AuthenticationUtils.logout(context);
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onUserDataSuccess(User user) {
        tvUsername.setText(user.getLogin());
        tvFirstName.setText(user.getFirstName());
        tvLastName.setText(user.getLastName());
        tvEmail.setText(user.getEmail());
        tvDateCreated.setText(user.getCreatedDate());

        if (user.getImageUrl() != null) {
            Glide.with(context)
                    .asBitmap()
                    .placeholder(R.drawable.application_logo)
                    .load(user.getImageUrl())
                    .into(ivProfilePic);
        }
    }

    @Override
    public void onUserDataFailure(Throwable throwable) {
        Toast.makeText(context, "Error receiving user data", Toast.LENGTH_SHORT).show();
    }
}
