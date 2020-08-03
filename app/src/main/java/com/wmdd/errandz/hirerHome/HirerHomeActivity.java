package com.wmdd.errandz.hirerHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.MaterialSharedAxis;
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

    private HirerHomeFragment hirerHomeFragment;
    private UserProfileFragment userProfileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_home);

        homeToolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        postJobFloatingActionButton = findViewById(R.id.add_job_action_float_button);

        createFragments();

        openHomeScreen();

        setViewClickListener();
    }

    private void createFragments() {
        hirerHomeFragment = new HirerHomeFragment();
        hirerHomeFragment.setEnterTransition(createTransition(false));
        hirerHomeFragment.newInstance(this);
        userProfileFragment = new UserProfileFragment();
        userProfileFragment.setEnterTransition(createTransition(true));
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
                        getSupportFragmentManager().beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.container,
                                userProfileFragment).commitAllowingStateLoss();
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


    private MaterialSharedAxis createTransition(boolean entering) {
        MaterialSharedAxis transition = new MaterialSharedAxis(MaterialSharedAxis.X, entering);
        // Add targets for this transition to explicitly run transitions only on these views. Without
        // targeting, a MaterialSharedAxis transition would be run for every view in the
        // Fragment's layout.
        transition.addTarget(R.id.root_client_home_screen);
        transition.addTarget(R.id.root_user_profile_screen);

        return transition;
    }

}