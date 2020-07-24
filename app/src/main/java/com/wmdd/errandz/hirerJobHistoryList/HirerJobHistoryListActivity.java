package com.wmdd.errandz.hirerJobHistoryList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.hirerPastJobDescription.HirerPastJobDescriptionActivity;
import com.wmdd.errandz.util.Constants;

import java.util.ArrayList;


public class HirerJobHistoryListActivity extends AppCompatActivity implements HirerJobsHistoryListAdapter.JobHistoryItemCLickListener{

    private static final int HIRER_HISTORY_LIST = 1;

    private MaterialToolbar toolbar;
    private RecyclerView upcomingJobsRecyclerView;
    private HirerJobsHistoryListAdapter hirerJobsHistoryListAdapter;
    private ArrayList<Job> jobArrayList;

    private HirerJobHistoryListViewModel hirerJobHistoryListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_upcoming_job);

        toolbar = findViewById(R.id.upcoming_job_toolbar);
        toolbar.setTitle("Job History");

        upcomingJobsRecyclerView = findViewById(R.id.upcoming_job_recycler_view);

        upcomingJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));

        hirerJobsHistoryListAdapter = new HirerJobsHistoryListAdapter(this);
        upcomingJobsRecyclerView.setAdapter(hirerJobsHistoryListAdapter);

        initializeViewModel();
    }

    private void initializeViewModel() {
        hirerJobHistoryListViewModel = ViewModelProviders.of(this).get(HirerJobHistoryListViewModel.class);
        hirerJobHistoryListViewModel.init();

        hirerJobHistoryListViewModel.getJobArrayList().observe(this, jobs -> {
            this.jobArrayList = jobs;
            hirerJobsHistoryListAdapter.setJobHistoryList(jobs);
        });

    }


    @Override
    public void onJobItemClicked(int position) {
        Intent intent = new Intent(this, HirerPastJobDescriptionActivity.class);
        intent.putExtra("JOB_ID", jobArrayList.get(position).getJobID());
        intent.putExtra("TASKER_ID", jobArrayList.get(position).getTaskerID());
        intent.putExtra(Constants.FROM_ACTIVITY, HIRER_HISTORY_LIST);
        startActivity(intent);
    }
}