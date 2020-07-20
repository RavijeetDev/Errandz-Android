package com.wmdd.errandz.hirerJobDescription;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;

public class HirerJobDescriptionActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ImageView jobCategoryImageView;
    private TextView jobWageTextView;
    private TextView jobNameTextView;
    private TextView jobDateTextView;
    private TextView jobCategoryTextView;
    private TextView jobDescriptionTextView;

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
    }
}