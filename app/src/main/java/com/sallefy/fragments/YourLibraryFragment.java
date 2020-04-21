package com.sallefy.fragments;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sallefy.R;
import com.sallefy.adapters.YourLibraryAdapter;
import com.sallefy.managers.playlists.MyPlaylistsCallback;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.managers.tracks.TrackCallback;
import com.sallefy.managers.tracks.TrackManager;
import com.sallefy.model.Playlist;
import com.sallefy.model.Track;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

public class YourLibraryFragment extends Fragment
        implements MyPlaylistsCallback, TrackCallback {

    private static YourLibraryFragment instance;
    private FragmentManager fragmentManager;
    private Context context;

    private ImageButton ibSettings;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private YourLibraryAdapter yourLibraryAdapter;
    private TextView mLibraryTitleTextView;

    public YourLibraryFragment(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.yourLibraryAdapter = new YourLibraryAdapter(context, fragmentManager);
        getMyPlaylists();
        getMyTracks();
    }

    public static YourLibraryFragment getInstance(Context context, FragmentManager fragmentManager) {
        if (instance == null) instance = new YourLibraryFragment(context, fragmentManager);
        if (instance.fragmentManager != fragmentManager) instance.fragmentManager = fragmentManager;
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

        ibSettings.setOnClickListener(view1 -> openProfileFragment());

        viewPager.setAdapter(yourLibraryAdapter);
        int gradientEnd = ResourcesCompat.getColor(getResources(), R.color.gradientEnd, null);
        int white = ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null);
        tabLayout.setSelectedTabIndicatorColor(gradientEnd);
        tabLayout.setTabTextColors(white, gradientEnd);
        new TabLayoutMediator(tabLayout, viewPager,
                ((tab, position) -> {
                    tab.setText((position == 0) ? "Playlists" : "Tracks");
                })
        ).attach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyPlaylists();
        getMyTracks();
    }

    private void initViews(View view) {
        ibSettings = view.findViewById(R.id.ib_settings);
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        mLibraryTitleTextView = view.findViewById(R.id.tv_your_library_title);

        TextPaint paint = mLibraryTitleTextView.getPaint();
        float width = paint.measureText(mLibraryTitleTextView.getText().toString());
        Shader shader = new LinearGradient(0, 0, width, 0,
                ContextCompat.getColor(view.getContext(), R.color.gradientStart),
                ContextCompat.getColor(view.getContext(), R.color.gradientEnd),
                Shader.TileMode.MIRROR);
        mLibraryTitleTextView.getPaint().setShader(shader);
    }

    private void getMyPlaylists() {
        PlaylistManager.getInstance().getMyPlaylists(context, this);
    }

    private void getMyTracks() {
        TrackManager.getInstance().getMyTracks(context, this);
    }

    private void openProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment(context);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_manager, profileFragment, "profileFragment")
                .addToBackStack(null)
                .commit();
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

    @Override
    public void onMyTracksReceived(List<Track> tracks) {
        yourLibraryAdapter.setTracks(tracks);
        viewPager.setAdapter(yourLibraryAdapter);
    }

    @Override
    public void onMyTracksFailure(Throwable throwable) {
        Toast.makeText(context, "Error receiving tracks", Toast.LENGTH_SHORT).show();
    }
}
