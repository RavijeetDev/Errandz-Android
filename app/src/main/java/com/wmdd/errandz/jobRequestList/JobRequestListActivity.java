package com.wmdd.errandz.jobRequestList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.User;

import com.wmdd.errandz.jobRequestTaskerInfo.JobRequestUserInfoActivity;

public class JobRequestListActivity extends AppCompatActivity implements JobRequestListAdapter.JobItemClickListener {

    private JobRequestListViewModel jobRequestListViewModel;

    private RecyclerView jobRequestListRecyclerView;
    private FrameLayout progressBarView;
    private JobRequestListAdapter jobRequestListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_request_list);

        jobRequestListRecyclerView = findViewById(R.id.job_request_recycler_view);
        progressBarView = findViewById(R.id.progress_bar_view);

        jobRequestListRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        jobRequestListAdapter = new JobRequestListAdapter(this);
        jobRequestListRecyclerView.setAdapter(jobRequestListAdapter);

        progressBarView.setVisibility(View.VISIBLE);
        initializeViewModel();
    }

    private void initializeViewModel() {
        jobRequestListViewModel = ViewModelProviders.of(this).get(JobRequestListViewModel.class);
        jobRequestListViewModel.init();

        jobRequestListViewModel.getJobArrayList().observe(this, jobs -> {
            if (jobs.size() > 0) {
                progressBarView.setVisibility(View.GONE);
                jobRequestListAdapter.setJobList(jobs);
            }
        });
    }

    @Override
    public void onJobItemClicked(User user) {
        Intent intent = new Intent(this, JobRequestUserInfoActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
    }
}