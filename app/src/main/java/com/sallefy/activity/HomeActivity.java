package com.sallefy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sallefy.R;
import com.sallefy.fragments.HomeFragment;
import com.sallefy.fragments.SearchFragment;
import com.sallefy.fragments.YourLibraryFragment;


public class HomeActivity extends FragmentActivity {

    private Context context;

    private BottomNavigationView mBottomNavigationView;
    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;
    private TextView tvMusicNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        initMusicNavView();
        setInitialFragment();
        this.context = getApplicationContext();
    }

    private void initMusicNavView() {
        tvMusicNav = findViewById(R.id.music_nav_title);
        tvMusicNav.setOnClickListener(v -> startActivity(new Intent(this, TrackActivity.class)));
    }

    private void initViews() {
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.action_home:
                    fragment = HomeFragment.getInstance();
                    break;
                case R.id.action_search:
                    fragment = SearchFragment.getInstance();
                    break;
                case R.id.action_your_library:
                    fragment = YourLibraryFragment.getInstance(context, mFragmentManager);
                    break;
            }
            changeFragment(fragment);
            return true;
        });
    }


    private void changeFragment(Fragment fragment) {
        String fragmentTag = ((Object) fragment).getClass().getName();
        Fragment currentFragment = mFragmentManager.findFragmentByTag(fragmentTag);
        try {
            if (currentFragment != null) {
                if (!currentFragment.isVisible()) {
                    if (fragment.getArguments() != null) {
                        currentFragment.setArguments(fragment.getArguments());
                    }
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_manager, currentFragment, fragmentTag)
                            .addToBackStack(null)
                            .commit();
                }
            } else {
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_manager, fragment, fragmentTag)
                        .addToBackStack(null)
                        .commit();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }

    private void setInitialFragment() {
        mTransaction.add(R.id.fragment_manager, HomeFragment.getInstance());
        mTransaction.commit();
    }

}
