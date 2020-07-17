package com.wmdd.errandz.hirerPostJob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.util.Constants;

public class HirerPostJobActivity extends AppCompatActivity implements View.OnClickListener {

    private HirerPostJobViewModel hirerPostJobViewModel;

    private TextInputLayout jobNameTextInputLayout;
    private TextInputLayout jobCategoryTextInputLayout;
    private TextInputLayout jobDateTextInputLayout;
    //    private TextInputLayout jobTimeTextInputLayout;
    private TextInputLayout jobWageTextInputLayout;
    private TextInputLayout jobDescriptionTextInputLayout;

    private FrameLayout datePickerContainer;
    //    private FrameLayout timePickerContainer;
    private Button postJobButton;
    private FrameLayout progressBarLayout;
    private int editTextLength;
    private boolean firstTimeAddingText;

    private Job job;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_post_job);

        job = getIntent().getParcelableExtra("JOB");

        jobNameTextInputLayout = findViewById(R.id.job_name_text_input_layout);
        jobCategoryTextInputLayout = findViewById(R.id.job_category_text_input_layout);
        jobDateTextInputLayout = findViewById(R.id.job_date_text_input_layout);
//        jobTimeTextInputLayout = findViewById(R.id.job_time_text_input_layout);
        jobWageTextInputLayout = findViewById(R.id.job_wage_text_input_layout);
        jobDescriptionTextInputLayout = findViewById(R.id.job_description_text_input_layout);
        datePickerContainer = findViewById(R.id.date_picker_container);
//        timePickerContainer = findViewById(R.id.time_picker_container);
        postJobButton = findViewById(R.id.post_job_button);
        progressBarLayout = findViewById(R.id.progress_bar_view);


        datePickerContainer.setOnClickListener(this);
        postJobButton.setOnClickListener(this);
//        timePickerContainer.setOnClickListener(this);

        initializeJobCategory();
        initializeViewModel();
//        addTextChangeListenerOnWageEditText(jobWageTextInputLayout.getEditText());

        if(job != null) {

            setJobData();
            postJobButton.setText("Update Job");
        }

    }

    private void setJobData() {
        jobNameTextInputLayout.getEditText().setText(job.getJobName());
        jobCategoryTextInputLayout.getEditText().setText(job.getJobCategoryName());
        jobDateTextInputLayout.getEditText().setText(job.getJobDate());
        jobWageTextInputLayout.getEditText().setText(job.getJobWage());
        jobDescriptionTextInputLayout.getEditText().setText(job.getDescription());
    }

    private void addTextChangeListenerOnWageEditText(EditText editText) {
        if(editText.getText().length() == 0){
            editTextLength = 0;
            firstTimeAddingText = true;
        } else {
            editTextLength = editText.getText().length() - 1;
            firstTimeAddingText = false;
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                String price = editText.getText().toString();
//                Log.e("Edit Text", String.valueOf(price.length()));
//                if(firstTimeAddingText || editTextLength != price.length()-1) {
//                    editTextLength++;
//                    firstTimeAddingText = false;
//                    editText.setText("$" + editText.getText().toString());
//                }
            }
        });
    }


    private void initializeJobCategory() {
        ArrayAdapter jobCategoryAutoCompleteTextViewAdater = new ArrayAdapter<>(this,
                R.layout.job_category_list_item, Constants.jobCategory);
        ((AutoCompleteTextView) jobCategoryTextInputLayout.getEditText())
                .setAdapter(jobCategoryAutoCompleteTextViewAdater);
    }

    private void initializeViewModel() {
        hirerPostJobViewModel = ViewModelProviders.of(this).get(HirerPostJobViewModel.class);
        hirerPostJobViewModel.init();


        hirerPostJobViewModel.getJobDateString().observe(this, jobDate -> {
            jobDateTextInputLayout.getEditText().setText(jobDate);
        });

        hirerPostJobViewModel.getPostJobResponse().observe(this, response -> {
            if(response.getStatus().equals("success")) {
                progressBarLayout.setVisibility(View.GONE);
                openJobDescriptionPage();
            }
        });
    }

    private void openJobDescriptionPage() {


//        Intent intent = new Intent(this, JobDescriptionActivity.class);
//        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_picker_container:
                hirerPostJobViewModel.openDatePicker(getSupportFragmentManager());
                break;
            case R.id.post_job_button:

                String jobName = jobNameTextInputLayout.getEditText().getText().toString();
                int jobCategory = getJobCategoryIndex();
                String jobWage = jobWageTextInputLayout.getEditText().getText().toString();
                String jobDescription = jobDescriptionTextInputLayout.getEditText().getText().toString();

                hirerPostJobViewModel.makePostJobCall(jobName, jobCategory, jobWage, jobDescription);
                break;
        }
    }

    private int getJobCategoryIndex() {
        String jobCategory = jobCategoryTextInputLayout.getEditText().getText().toString();
        for (int i=0; i<Constants.jobCategory.length; i++) {
            if(Constants.jobCategory[i].equals(jobCategory)) {
                return i;
            }
        }
        return 0;
    }



}