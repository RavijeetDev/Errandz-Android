package com.wmdd.errandz.taskerHomeScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wmdd.errandz.R;
import com.wmdd.errandz.taskerJobHistory.TaskerJobHistoryListFragment;
import com.wmdd.errandz.taskersJobs.TaskerJobsActivity;
import com.wmdd.errandz.userProfile.UserProfileFragment;

public class TaskerHomeActivity extends AppCompatActivity {

    private MaterialToolbar homeToolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasker_home);

        homeToolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        setViewClickListener();
        openHomeScreen();
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
                    case R.id.my_jobs_menu:
                        homeToolbar.setTitle("My Jobs");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                new TaskerJobsActivity()).commitAllowingStateLoss();
                        return true;
                    case R.id.job_history_menu:
                        homeToolbar.setTitle("My History");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                new TaskerJobHistoryListFragment()).commitAllowingStateLoss();
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

    }

    private void openHomeScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new TaskerHomeFragment()).commitAllowingStateLoss();
    }
}