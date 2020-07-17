package com.wmdd.errandz.hirerHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wmdd.errandz.R;
import com.wmdd.errandz.hirerPostJob.HirerPostJobActivity;
import com.wmdd.errandz.userProfile.UserProfileFragment;

public class HirerHomeActivity extends AppCompatActivity implements HirerHomeFragment.Callback {

    private HirerHomeViewModel hirerHomeViewModel;

    private ConstraintLayout postFirstJobLayoutContainer;
    private CardView addJobButtonContainer;
    private FrameLayout progressBarLayout;
    private FloatingActionButton postJobFloatingActionButton;
    private MaterialToolbar homeToolbar;

    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_home);

        homeToolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        postJobFloatingActionButton = findViewById(R.id.add_job_action_float_button);

        openHomeScreen();

        setViewClickListener();
    }

    private void setViewClickListener() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                homeToolbar.getMenu().clear();
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        homeToolbar.setTitle("Errandz");
                        openHomeScreen();
                        return true;
                    case R.id.profile_menu:
                        homeToolbar.setTitle("User Profile");
                        homeToolbar.inflateMenu(R.menu.menu_home);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                new UserProfileFragment()).commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });

        postJobFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HirerHomeActivity.this, HirerPostJobActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openHomeScreen() {
        HirerHomeFragment hirerHomeFragment = new HirerHomeFragment();
        hirerHomeFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, hirerHomeFragment)
                .commitAllowingStateLoss();
    }


    @Override
    public void onListFetched(boolean isThereAnyJobPosted) {
        if(isThereAnyJobPosted) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            postJobFloatingActionButton.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
            postJobFloatingActionButton.setVisibility(View.GONE);
        }
    }


}