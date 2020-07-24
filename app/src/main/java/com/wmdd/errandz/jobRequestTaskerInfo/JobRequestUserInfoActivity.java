package com.wmdd.errandz.jobRequestTaskerInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.userInfoWithReviewList.UserInfoWithReviewListViewModel;

public class JobRequestUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView jobRequestUserInfoRecyclerView;
    private FrameLayout progressBarView;
    private Button acceptButton;
    private Button rejectButton;

    private JobRequestUserInfoAdapter jobRequestUserInfoAdapter;
    private JobRequestUserInfoViewModel jobRequestUserInfoViewModel;

    private User user;
    private int jobStatusID;
    private int jobID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_request_user_info);

        jobID = getIntent().getIntExtra("JOB_ID", 0);
        jobStatusID = getIntent().getIntExtra("JOB_STATUS_ID", 0);
        user = getIntent().getParcelableExtra("USER");

        jobRequestUserInfoRecyclerView = findViewById(R.id.user_info_recycler_view);
        acceptButton = findViewById(R.id.apply_button);
        rejectButton = findViewById(R.id.save_button);
        progressBarView = findViewById(R.id.progress_bar_view);

        jobRequestUserInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        jobRequestUserInfoAdapter = new JobRequestUserInfoAdapter(user);
        jobRequestUserInfoRecyclerView.setAdapter(jobRequestUserInfoAdapter);

        acceptButton.setOnClickListener(this);
        rejectButton.setOnClickListener(this);

        initializeViewModel();

    }

    private void initializeViewModel() {
        jobRequestUserInfoViewModel = ViewModelProviders.of(this).get(JobRequestUserInfoViewModel.class);
        jobRequestUserInfoViewModel.init();
        jobRequestUserInfoViewModel.makeUserReviewApiCall(user.getUserID());
        progressBarView.setVisibility(View.VISIBLE);

        jobRequestUserInfoViewModel.getReviewArrayList().observe(this, reviews -> {
            progressBarView.setVisibility(View.GONE);

            if(reviews.size() > 0) {
                jobRequestUserInfoAdapter.setReviewArrayList(reviews);
            }
        });

        jobRequestUserInfoViewModel.getJobStatusResponse().observe(this, response -> {
            progressBarView.setVisibility(View.GONE);

            if(response.getStatus().equals("success")) {
                Toast.makeText(JobRequestUserInfoActivity.this, "Job request updated successfully !!",
                        Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.apply_button:
                jobRequestUserInfoViewModel.callUpdateJobStatusApi(2, jobStatusID, jobID, user.getUserID());
                break;
            case R.id.save_button:
                jobRequestUserInfoViewModel.callUpdateJobStatusApi(3, jobStatusID, jobID, user.getUserID());
                break;
        }
    }
}