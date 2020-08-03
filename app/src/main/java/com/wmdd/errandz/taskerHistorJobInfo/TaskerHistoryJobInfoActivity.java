package com.wmdd.errandz.taskerHistorJobInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.model.CircleOptions;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.Review;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.hirerPastJobDescription.HirerPastJobDescriptionViewModel;
import com.wmdd.errandz.util.Constants;

import java.util.ArrayList;

public class TaskerHistoryJobInfoActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private static final int HIRER_HISTORY_LIST = 1;

    private ImageView jobCategoryImageView;
    private ImageView taskerProfileImageView;
    private TextView jobWageTextView;
    private TextView jobNameTextView;
    private TextView jobDateTextView;
    private TextView jobCategoryTextView;
    private TextView jobDescriptionTextView;
    private TextView taskerNameTextView;
    private TextView taskerRatingTextView;
    private TextView taskerNumberOfReviewsTextView;
    private RatingBar taskerRatingBar;
    private TextView reviewLabel;
    private CardView hirerReviewContainer;
    private ImageView hirerReviewEditButton;
    private ImageView hirerProfileImageView;
    private TextView hirerNameTextView;
    private RatingBar hirerRatingBar;
    private TextView hirerReviewPostedDateTextView;
    private TextView hirerReviewTextView;
    private TextInputLayout writeReviewTextInputLayout;

    private FloatingActionButton writeReviewButton;
    private Button postButton;
    private TextView noReviewsTextView;
    private FrameLayout progressBarLayout;

    private Menu menu;
    private MaterialToolbar toolbar;

    private User user;
    private int jobID;
    private int hirerID;
    private int fromActivity;

    private TaskerHistoryJonInfoViewModel taskerHistoryJonInfoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tasker_history_job_info);

        toolbar = findViewById(R.id.add_job_toolbar);
        jobCategoryImageView = findViewById(R.id.job_category_image_view);
        jobWageTextView = findViewById(R.id.job_wage_label);
        jobNameTextView = findViewById(R.id.job_name_label);
        jobDateTextView = findViewById(R.id.job_date_label);
        jobCategoryTextView = findViewById(R.id.job_category_label);
        taskerProfileImageView = findViewById(R.id.profile_image_view);
        taskerNameTextView = findViewById(R.id.hirer_name_text_view);
        taskerRatingTextView = findViewById(R.id.hirer_rating_text_view);
        taskerRatingBar = findViewById(R.id.hirer_rating_bar);
        taskerNumberOfReviewsTextView = findViewById(R.id.hirer_reviews_count_text_view);
        reviewLabel = findViewById(R.id.review_label);

        hirerReviewContainer = findViewById(R.id.user_review);
        hirerReviewEditButton = findViewById(R.id.edit_button);
        hirerProfileImageView = findViewById(R.id.user_profile_image_view);
        hirerNameTextView = findViewById(R.id.user_name_text_view);
        hirerRatingBar = findViewById(R.id.user_review_rating_bar);
        hirerReviewPostedDateTextView = findViewById(R.id.user_review_posted_date_text_view);
        hirerReviewTextView = findViewById(R.id.user_review_text_view);
        writeReviewTextInputLayout = findViewById(R.id.write_review_text_input_layout);

        postButton = findViewById(R.id.post_button);
        noReviewsTextView = findViewById(R.id.no_review_posted);
        writeReviewButton = findViewById(R.id.write_review_button);

        progressBarLayout = findViewById(R.id.progress_bar_view);

        jobID = getIntent().getIntExtra("JOB_ID", 0);
        hirerID = getIntent().getIntExtra("HIRER_ID", 0);
        fromActivity = getIntent().getIntExtra(Constants.FROM_ACTIVITY, 0);

        writeReviewButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
        hirerReviewEditButton.setOnClickListener(this);

        initializeViewModel();
        initializeView();

    }

    private void initializeViewModel() {

        taskerHistoryJonInfoViewModel = ViewModelProviders.of(this).get(TaskerHistoryJonInfoViewModel.class);
        taskerHistoryJonInfoViewModel.init();

        taskerHistoryJonInfoViewModel.makeHistoryJobInfoApiCall(hirerID, jobID);
        progressBarLayout.setVisibility(View.VISIBLE);

        taskerHistoryJonInfoViewModel.getJobMutableLiveData().observe(this, job -> {
            setJobValues(job);

        });
        taskerHistoryJonInfoViewModel.getUserMutableLiveData().observe(this, user -> {
            this.user = user;
            setUserValues(user);
            taskerHistoryJonInfoViewModel.makeJobReviewApiCall(jobID);

        });

        taskerHistoryJonInfoViewModel.getReviewList().observe(this, reviews -> {
            progressBarLayout.setVisibility(View.GONE);
            reviewLabel.setVisibility(View.VISIBLE);
            if (reviews.size() > 0) {
                writeReviewTextInputLayout.setVisibility(View.GONE);
                postButton.setVisibility(View.GONE);
                writeReviewButton.setVisibility(View.GONE);
                showReviews(reviews);
            } else {
                noReviewsTextView.setVisibility(View.VISIBLE);
                writeReviewButton.setVisibility(View.VISIBLE);
            }
        });

        taskerHistoryJonInfoViewModel.getNewReviewAddedResponse().observe(this, response -> {
            taskerHistoryJonInfoViewModel.makeHistoryJobInfoApiCall(hirerID, jobID);
        });

    }

    private void showReviews(ArrayList<Review> reviews) {

        Review review = reviews.get(0);
        hirerReviewContainer.setVisibility(View.VISIBLE);
        hirerNameTextView.setText(review.getReviewerName());
        hirerRatingBar.setRating(review.getRating());
        hirerReviewPostedDateTextView.setText("Reviewed On: " + review.getReviewDate());
        hirerReviewTextView.setText(review.getReview());

        Picasso.get()
                .load(review.getReviewerProfilePic())
                .into(hirerProfileImageView);


    }

    private void setUserValues(User user) {
        Picasso.get()
                .load(user.getProfileImage())
                .into(taskerProfileImageView);

        taskerNameTextView.setText(user.getFirstName() + " " + user.getLastName());

        if (user.getTotalRating() > 0) {
            taskerRatingTextView.setText(String.format("%.1f", user.getTotalRating()));
            taskerRatingBar.setRating((float) (user.getTotalRating() / user.getNumberOfReviews()));
        } else {
            taskerRatingTextView.setText("No Rating");
            taskerRatingBar.setVisibility(View.GONE);
        }

        taskerNumberOfReviewsTextView.setText(user.getNumberOfReviews() == 0 || user.getNumberOfReviews() == 1
                ? user.getNumberOfReviews() + " Review" : user.getNumberOfReviews() + " Reviews");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setJobValues(Job job) {

        jobCategoryImageView.setImageResource(job.getJobCategoryImage());
        jobWageTextView.setText("$" + job.getJobWage());
        jobNameTextView.setText(job.getJobName());
        jobDateTextView.setText(job.getJobDate());
        jobCategoryTextView.setText(job.getJobCategoryName());

    }

    private void initializeMenuClickListener() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.user_edit_menu) {
//                    Intent intent = new Intent(HirerJobDescriptionActivity.this, HirerPostJobActivity.class);
//                    intent.putExtra("JOB", job);
//                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void initializeView() {

//        if (fromActivity == HIRER_HISTORY_LIST) {
//
//        } else {
//            toolbar.inflateMenu(R.menu.menu_home);
//            menu = toolbar.getMenu();
//            initializeMenuClickListener();
//        }

    }


    @Override
    public void onClick(View v) {
        noReviewsTextView.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.write_review_button:
                reviewLabel.setVisibility(View.GONE);
                writeReviewTextInputLayout.setVisibility(View.VISIBLE);
                postButton.setVisibility(View.VISIBLE);
                writeReviewButton.setVisibility(View.GONE);
                break;
            case R.id.post_button:
                progressBarLayout.setVisibility(View.VISIBLE);
                float rating = 4.0f;
                String review = writeReviewTextInputLayout.getEditText().getText().toString();
                taskerHistoryJonInfoViewModel.makeNewReviewCall(jobID, hirerID, rating, review);
                break;
            case R.id.edit_button:
                writeReviewTextInputLayout.setVisibility(View.VISIBLE);
                postButton.setVisibility(View.VISIBLE);
                writeReviewButton.setVisibility(View.GONE);
                reviewLabel.setVisibility(View.GONE);
                hirerReviewContainer.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLng position = new LatLng(Double.parseDouble(user.getAddress().getLatitude()), Double.parseDouble(user.getAddress().getLongitude()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                googleMap.addCircle(new CircleOptions()
                        .center(position)
                        .radius(750)
                        .strokeColor(ContextCompat.getColor(getApplicationContext(), R.color.map_stroke))
                        .fillColor(ContextCompat.getColor(getApplicationContext(), R.color.map_fill)));
                googleMap.setMinZoomPreference(13.0f);
                googleMap.setMaxZoomPreference(13.0f);
            }
        });
    }
}