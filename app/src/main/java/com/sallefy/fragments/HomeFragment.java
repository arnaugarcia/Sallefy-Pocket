package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sallefy.R;
import com.sallefy.adapters.UserListAdapter;
import com.sallefy.managers.user.PopularUsersCallback;
import com.sallefy.managers.user.UserManager;
import com.sallefy.model.User;

import java.util.List;

public class HomeFragment extends Fragment implements PopularUsersCallback {

    private static HomeFragment instance;
    private Context context;

    private static final String TAG = "HomeFragment";

    private RecyclerView rvArtists;
    private RecyclerView rvPlaylists;
    private RecyclerView rvTracks;

    public HomeFragment(Context context) {
        this.context = context;
    }

    public static HomeFragment getInstance(Context context) {
        if (instance == null) instance = new HomeFragment(context);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPopularUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view) {
        rvArtists = view.findViewById(R.id.rv_artists);
        rvPlaylists = view.findViewById(R.id.rv_playlists);
        rvTracks = view.findViewById(R.id.rv_tracks);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getPopularUsers() {
        UserManager.getInstance().getPopularUsers(getContext(), this);
    }

    @Override
    public void onPopularUsersSuccess(List<User> users) {
        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        UserListAdapter adapter = new UserListAdapter(context, users);
        rvArtists.setLayoutManager(manager);
        rvArtists.setAdapter(adapter);
    }

    @Override
    public void onPopularUsersFailure(Throwable throwable) {
        Log.d(TAG, "onPopularUsersFailure: " + throwable.getMessage());
    }
}
