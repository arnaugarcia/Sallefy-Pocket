package com.sallefy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sallefy.R;
import com.sallefy.adapters.YourLibraryAdapter;
import com.sallefy.managers.playlists.PlaylistCallback;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.model.Playlist;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class YourLibraryFragment extends Fragment implements PlaylistCallback {

    private static YourLibraryFragment instance;
    private Context context;

    private ViewPager2 viewPager;
    private YourLibraryAdapter yourLibraryAdapter;

    public YourLibraryFragment(Context context) {
        this.context = context;
        yourLibraryAdapter = new YourLibraryAdapter(context);
        getMyPlaylists();
    }

    public static YourLibraryFragment getInstance(Context context) {
        if (instance == null) instance = new YourLibraryFragment(context);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews(View view) {
        viewPager = view.findViewById(R.id.view_pager);
    }

    private void getMyPlaylists() {
        PlaylistManager.getInstance().getMyPlaylists(context, this);
    }

    @Override
    public void onMyPlaylistsReceived(List<Playlist> playlists) {
        yourLibraryAdapter.setPlaylists(playlists);
        viewPager.setAdapter(yourLibraryAdapter);
    }

    @Override
    public void onMyPlaylistsFailure(Throwable throwable) {
        Toast.makeText(context, "Error receiving playlists", Toast.LENGTH_SHORT).show();
    }
}
