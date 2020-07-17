package com.wmdd.errandz.taskerJobDescription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeViewModel;
import com.wmdd.errandz.userInfoWithReviewList.UserInfoWithReviewListActivity;
import com.wmdd.errandz.util.Constants;

public class TaskerJobDescriptionActivity extends AppCompatActivity {

    private MaterialToolbar jobDescriptionToolbar;
    private Menu menu;
    private ScrollView jobDescriptionContainer;
    private ImageView jobCategoryImageView;
    private ImageView hirerProfileImageView;
    private TextView wageTextView;
    private TextView jobStatusTextView;
    private TextView jobNameTextView;
    private TextView jobDateTextView;
    private TextView jobCategoryTextView;
    private TextView hirerNameTextView;
    private TextView hirerRatingTextView;
    private TextView hirerReviewsCountTextView;
    private TextView jobDescriptionTextView;
    private RatingBar hirerRatingRatingBar;
    private Button applyButton;
    private Button saveButton;
    private FrameLayout progressBarLayout;
    private FrameLayout buttonBackgroundBehindContainer;

    private TaskerJobDescriptionViewModel taskerJobDescriptionViewModel;

    private Job job;
    private User user;
    private int jobID;
    private int hirerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasker_job_description);

        jobDescriptionToolbar = findViewById(R.id.job_description_toolbar);
        jobDescriptionContainer = findViewById(R.id.job_description_container);
        jobCategoryImageView = findViewById(R.id.job_category_image_view);
        hirerProfileImageView = findViewById(R.id.user_profile_image_view);
        wageTextView = findViewById(R.id.job_wage_label);
        jobStatusTextView = findViewById(R.id.job_status_text_view);
        jobNameTextView = findViewById(R.id.job_name_label);
        jobDateTextView = findViewById(R.id.job_date_label);
        jobCategoryTextView = findViewById(R.id.job_category_label);
        hirerNameTextView = findViewById(R.id.hirer_name_text_view);
        hirerRatingTextView = findViewById(R.id.hirer_rating_text_view);
        hirerReviewsCountTextView = findViewById(R.id.hirer_reviews_count_text_view);
        jobDescriptionTextView = findViewById(R.id.job_description_text_view);
        hirerRatingRatingBar = findViewById(R.id.hirer_rating_bar);
        applyButton = findViewById(R.id.apply_button);
        saveButton = findViewById(R.id.save_button);
        progressBarLayout = findViewById(R.id.progress_bar_view);
        buttonBackgroundBehindContainer = findViewById(R.id.button_background_behind_container);

        jobID = getIntent().getIntExtra("JOB_ID", 0);
        hirerID = getIntent().getIntExtra("HIRER_ID", 0);

        initializeViewModel();
        initializeClickListener();

    }

    private void initializeViewModel() {
        taskerJobDescriptionViewModel = ViewModelProviders.of(this).get(TaskerJobDescriptionViewModel.class);
        taskerJobDescriptionViewModel.init();

        taskerJobDescriptionViewModel.makeJobInfoApiCall(hirerID, jobID);
        progressBarLayout.setVisibility(View.VISIBLE);

        taskerJobDescriptionViewModel.getUserMutableLiveData().observe(this, user -> {
            this.user = user;
            initializeUserInfoValues(user);
        });

        taskerJobDescriptionViewModel.getJobMutableLiveData().observe(this, job -> {
            this.job = job;
            initializeJobInfoValues(job);
        });

        taskerJobDescriptionViewModel.getResponseMutableLiveData().observe(this, response -> {
            if (response.getStatus().equals("success")) {
                progressBarLayout.setVisibility(View.GONE);
                finish();
            }
        });

        taskerJobDescriptionViewModel.getSavedMutableLiveData().observe(this, response -> {
            if (response.getStatus().equals("success")) {
                taskerJobDescriptionViewModel.makeJobInfoApiCall(hirerID, jobID);
            }
        });

    }

    private void initializeClickListener() {
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarLayout.setVisibility(View.VISIBLE);
                taskerJobDescriptionViewModel.makeApplyJobApiCall(job);

            }
        });

        hirerProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskerJobDescriptionActivity.this, UserInfoWithReviewListActivity.class);
                intent.putExtra("USER", user);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarLayout.setVisibility(View.VISIBLE);
                if (job.getStatus() == 4) {
                    taskerJobDescriptionViewModel.unsaveJob(job);
                } else {
                    taskerJobDescriptionViewModel.saveJob(job);
                }
            }
        });
    }

    private void initializeJobInfoValues(Job job) {
        progressBarLayout.setVisibility(View.GONE);
        jobDescriptionContainer.setVisibility(View.VISIBLE);

        jobCategoryImageView.setImageResource(job.getJobCategoryImage());
        wageTextView.setText("Wage: $" + job.getJobWage());
        jobNameTextView.setText(job.getJobName());
        jobDateTextView.setText(job.getJobDate());
        jobCategoryTextView.setText(job.getJobCategoryName());
        jobDescriptionTextView.setText(job.getDescription());


        if (job.getStatus() == 1 || job.getStatus() == 2 || job.getStatus() == 3) {

        }
        switch (job.getStatus()) {
            case 0:
                saveButton.setText("Save");
            case 1:
                jobStatusTextView.setText("Waiting for Approval");
                break;
            case 2:
                jobStatusTextView.setText("Approved");
                break;
            case 3:
                jobStatusTextView.setText("Rejected");
            case 4:
                saveButton.setText("Unsave");
        }

        if (job.getStatus() != 0 && job.getStatus() != 4) {
            saveButton.setVisibility(View.GONE);
            jobStatusTextView.setVisibility(View.VISIBLE);
            applyButton.setVisibility(View.GONE);
            buttonBackgroundBehindContainer.setVisibility(View.GONE);
        }

    }

    private void initializeUserInfoValues(User user) {

        hirerNameTextView.setText(user.getFirstName() + " " + user.getLastName());

        if (user.getTotalRating() > 0) {
            hirerRatingTextView.setText(String.format("%.1f", user.getTotalRating()));
            hirerRatingRatingBar.setRating(user.getTotalRating()/5);
        } else {
            hirerRatingTextView.setText("No Rating");
            hirerRatingRatingBar.setVisibility(View.GONE);
        }

        hirerReviewsCountTextView.setText(user.getNumberOfReviews() == 0 || user.getNumberOfReviews() == 1
                ? user.getNumberOfReviews() + " Review" : user.getNumberOfReviews() + " Reviews");

        Picasso.get()
                .load(user.getProfileImage())
                .into(hirerProfileImageView);

    }
}