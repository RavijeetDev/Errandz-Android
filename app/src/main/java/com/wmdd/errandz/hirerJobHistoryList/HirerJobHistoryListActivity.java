package com.wmdd.errandz.hirerJobHistoryList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.wmdd.errandz.R;


public class HirerJobHistoryListActivity extends AppCompatActivity implements HirerJobsHistoryListAdapter.JobHistoryItemCLickListener{

    private RecyclerView upcomingJobsRecyclerView;
    private HirerJobsHistoryListAdapter hirerJobsHistoryListAdapter;

    private HirerJobHistoryListViewModel hirerJobHistoryListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_upcoming_job);

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
            hirerJobsHistoryListAdapter.setJobHistoryList(jobs);
        });

    }



    @Override
    public void onJobItemClicked(int position) {

    }
}