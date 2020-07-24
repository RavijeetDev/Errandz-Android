package com.wmdd.errandz.jobRequestTaskerInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.userInfoWithReviewList.UserInfoWithReviewListViewModel;

public class JobRequestUserInfoActivity extends AppCompatActivity {

    private RecyclerView jobRequestUserInfoRecyclerView;
    private FrameLayout progressBarView;

    private JobRequestUserInfoAdapter jobRequestUserInfoAdapter;
    private JobRequestUserInfoViewModel jobRequestUserInfoViewModel;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_request_user_info);

        user = getIntent().getParcelableExtra("USER");

        jobRequestUserInfoRecyclerView = findViewById(R.id.user_info_recycler_view);
        progressBarView = findViewById(R.id.progress_bar_view);

        jobRequestUserInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        jobRequestUserInfoAdapter = new JobRequestUserInfoAdapter(user);
        jobRequestUserInfoRecyclerView.setAdapter(jobRequestUserInfoAdapter);

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



    }

}