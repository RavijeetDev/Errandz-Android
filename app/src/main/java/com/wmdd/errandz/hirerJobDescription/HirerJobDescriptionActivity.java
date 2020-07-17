package com.wmdd.errandz.hirerJobDescription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.hirerPostJob.HirerPostJobActivity;
import com.wmdd.errandz.util.Constants;

public class HirerJobDescriptionActivity extends AppCompatActivity {

    private ImageView jobCategoryImageView;
    private TextView jobWageTextView;
    private TextView jobNameTextView;
    private TextView jobDateTextView;
    private TextView jobCategoryTextView;
    private TextView jobDescriptionTextView;
    private Menu menu;
    private MaterialToolbar toolbar;

    private Job job;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_job_description);

        toolbar = findViewById(R.id.add_job_toolbar);
        jobCategoryImageView = findViewById(R.id.job_category_image_view);
        jobWageTextView = findViewById(R.id.job_wage_label);
        jobNameTextView = findViewById(R.id.job_name_label);
        jobDateTextView = findViewById(R.id.job_date_label);
        jobCategoryTextView = findViewById(R.id.job_category_label);
        jobDescriptionTextView = findViewById(R.id.job_description_text_view);

        toolbar.inflateMenu(R.menu.menu_home);
        menu = toolbar.getMenu();

        job = getIntent().getParcelableExtra("JOB");

        initializeView();
        initializeMenuClickListener();
    }

    private void initializeMenuClickListener() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.user_edit_menu) {
                    Intent intent = new Intent(HirerJobDescriptionActivity.this, HirerPostJobActivity.class);
                    intent.putExtra("JOB", job);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void initializeView() {
        jobCategoryImageView.setImageResource(job.getJobCategoryImage());
        jobWageTextView.setText("$"+job.getJobWage());
        jobNameTextView.setText(job.getJobName());
        jobDateTextView.setText(job.getJobDate());
        jobCategoryTextView.setText(job.getJobCategoryName());
        jobDescriptionTextView.setText(job.getDescription());
    }


}