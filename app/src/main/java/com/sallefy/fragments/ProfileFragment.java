package com.sallefy.fragments;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sallefy.R;
import com.sallefy.managers.user.UserCallback;
import com.sallefy.managers.user.UserManager;
import com.sallefy.model.User;

public class ProfileFragment extends Fragment implements UserCallback {

    private Context context;

    private TextView tvProfileTitle;
    private CircularImageView ivProfilePic;
    private TextView tvUsername;
    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvEmail;
    private TextView tvDateCreated;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        TextPaint paint = tvProfileTitle.getPaint();
        float width = paint.measureText(tvProfileTitle.getText().toString());
        Shader shader = new LinearGradient(0, 0, width, 0,
                ContextCompat.getColor(view.getContext(), R.color.gradientStart),
                ContextCompat.getColor(view.getContext(), R.color.gradientEnd),
                Shader.TileMode.MIRROR);
        tvProfileTitle.getPaint().setShader(shader);
    }

    private void initViews(View view) {
        tvProfileTitle = view.findViewById(R.id.tv_profile_title);
        ivProfilePic = view.findViewById(R.id.iv_profile_pic);
        tvUsername = view.findViewById(R.id.tv_login);
        tvFirstName = view.findViewById(R.id.tv_first_name);
        tvLastName = view.findViewById(R.id.tv_last_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvDateCreated = view.findViewById(R.id.tv_date_created);
    }

    private void getUserData() {
        UserManager.getInstance().getUserData(context, this);
    }

    @Override
    public void onUserDataReceived(User user) {
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
