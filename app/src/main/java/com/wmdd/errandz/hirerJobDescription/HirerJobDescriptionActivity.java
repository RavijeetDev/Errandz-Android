package com.wmdd.errandz.hirerJobDescription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;

public class HirerJobDescriptionActivity extends AppCompatActivity {

    private HirerJobDescriptionViewModel hirerJobDescriptionViewModel;

    private MaterialToolbar toolbar;
    private ImageView jobCategoryImageView;
    private TextView jobWageTextView;
    private TextView jobNameTextView;
    private TextView jobDateTextView;
    private TextView jobCategoryTextView;
    private TextView jobDescriptionTextView;
    private TextView assignedToLabel;
    private TextView hirerNameTextView;
    private TextView hirerEmailTextView;
    private ImageView userProfileImageView;
    private FrameLayout userProfileContainer;

    private Job job;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_job_description);

        toolbar = findViewById(R.id.job_description_toolbar);
        jobCategoryImageView = findViewById(R.id.job_category_image_view);
        jobWageTextView = findViewById(R.id.job_wage_label);
        jobNameTextView = findViewById(R.id.job_name_label);
        jobDateTextView = findViewById(R.id.job_date_label);
        jobCategoryTextView = findViewById(R.id.job_category_label);
        jobDescriptionTextView = findViewById(R.id.job_description_text_view);
        assignedToLabel = findViewById(R.id.assigned_to_heading_label);
        userProfileContainer = findViewById(R.id.user_profile_container);
        userProfileImageView = findViewById(R.id.user_profile_image_view);
        hirerNameTextView = findViewById(R.id.hirer_name_text_view);
        hirerEmailTextView = findViewById(R.id.hirer_email_text_view);

        initializeViewModel();
        initializeView();


    }

    private void initializeView() {

        job = getIntent().getParcelableExtra("JOB");

        jobCategoryImageView.setImageResource(job.getJobCategoryImage());
        jobWageTextView.setText("$" + job.getJobWage());
        jobNameTextView.setText(job.getJobName());
        jobDateTextView.setText(job.getJobDate());
        jobCategoryTextView.setText(job.getJobCategoryName());
        jobDescriptionTextView.setText(job.getDescription());

        hirerJobDescriptionViewModel.makeJobInfoAPiCall(job.getJobID());
    }

    private void initializeViewModel() {
        hirerJobDescriptionViewModel = ViewModelProviders.of(this).get(HirerJobDescriptionViewModel.class);
        hirerJobDescriptionViewModel.init();

        hirerJobDescriptionViewModel.getJobDescriptionMutableLiveData().observe(this, jobDescription -> {

            if(jobDescription != null && jobDescription.getUser() != null) {
                assignedToLabel.setVisibility(View.VISIBLE);
                userProfileContainer.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(jobDescription.getUser().getProfileImage())
                        .into(userProfileImageView);

                hirerEmailTextView.setVisibility(View.VISIBLE);
                hirerEmailTextView.setText(jobDescription.getUser().getEmailID());
                hirerNameTextView.setVisibility(View.VISIBLE);
                hirerNameTextView.setText(jobDescription.getUser().getFirstName()+ " " + jobDescription.getUser().getLastName());
            }
        });

    }
}