package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements UserCallback {

    private Context context;

    private CircularImageView ivProfilePic;
    private TextView tvUsername;
    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvEmail;
    private TextView tvDateCreated;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(Context context) {
        this.context = context;
        getUserData();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view) {
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
