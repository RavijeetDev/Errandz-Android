package com.wmdd.errandz.hirerUpcomingJobList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.hirerJobDescription.HirerJobDescriptionActivity;
import com.wmdd.errandz.hirerPastJobDescription.HirerPastJobDescriptionActivity;

import java.util.ArrayList;

public class HirerUpcomingJobListActivity extends AppCompatActivity implements HirerUpcomingJobListAdapter.UpcomingJobItemCLickListener {

    private MaterialToolbar toolbar;
    private RecyclerView upcomingJobsRecyclerView;
    private HirerUpcomingJobListAdapter hirerUpcomingJobListAdapter;

    private HirerUpcomingJobListViewModel hirerUpcomingJobListViewModel;
    private ArrayList<Job> jobArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_upcoming_job);
        toolbar = findViewById(R.id.upcoming_job_toolbar);
        toolbar.setTitle("Upcoming Jobs");

        upcomingJobsRecyclerView = findViewById(R.id.upcoming_job_recycler_view);

        upcomingJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));

        hirerUpcomingJobListAdapter = new HirerUpcomingJobListAdapter(this);
        upcomingJobsRecyclerView.setAdapter(hirerUpcomingJobListAdapter);

        jobArrayList = new ArrayList<>();

        initializeViewModel();
    }

    private void initializeViewModel() {
        hirerUpcomingJobListViewModel = ViewModelProviders.of(this).get(HirerUpcomingJobListViewModel.class);
        hirerUpcomingJobListViewModel.init();

        hirerUpcomingJobListViewModel.getJobArrayList().observe(this, jobs -> {
            jobArrayList = jobs;
            hirerUpcomingJobListAdapter.setUpcomingJobList(jobs);
        });

    }



    @Override
    public void onJobItemClicked(int position) {
        Intent jobDescriptionIntent = new Intent(this, HirerJobDescriptionActivity.class);
        jobDescriptionIntent.putExtra("JOB", jobArrayList.get(position));
        startActivity(jobDescriptionIntent);
    }
}