package com.wmdd.errandz.userInfoWithReviewList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.taskerJobDescription.TaskerJobDescriptionViewModel;

public class UserInfoWithReviewListActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ImageView userProfileImageView;
    private TextView userNameTextView;
    private TextView userAgeTextView;
    private TextView userRatingTextView;
    private RatingBar userRatingBar;
    private TextView bioTextView;
    private TextView bioLabel;
    private RecyclerView reviewsRecycleView;
    private TextView noReviewsTextView;
    private FrameLayout progressBarLayout;

    private UserInfoWithReviewListViewModel userInfoWithReviewListViewModel;
    private ReviewListAdapter reviewListAdapter;

    private User user;
    private String fromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_with_review_list);

        toolbar = findViewById(R.id.user_info_toolbar);
        userProfileImageView = findViewById(R.id.user_profile_image_view);
        userNameTextView = findViewById(R.id.user_name_text_view);
        userAgeTextView = findViewById(R.id.user_dob_text_view);
        userRatingTextView = findViewById(R.id.user_rating_text_view);
        userRatingBar = findViewById(R.id.user_rating_bar);
        bioTextView = findViewById(R.id.bio_text_view);
        bioLabel = findViewById(R.id.label_bio);
        reviewsRecycleView = findViewById(R.id.review_recycler_view);
        noReviewsTextView = findViewById(R.id.no_review_text_view);
        progressBarLayout = findViewById(R.id.progress_bar_view);

        reviewsRecycleView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        reviewListAdapter = new ReviewListAdapter();
        reviewsRecycleView.setAdapter(reviewListAdapter);


        setUserInfo();
        initializeViewModel();
    }

    private void setUserInfo() {

        user = getIntent().getParcelableExtra("USER");
        fromActivity = getIntent().getStringExtra("FROM_ACTIVITY");

        setToolbarTitle();

        userNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        userAgeTextView.setText("Age: " + user.getAge());

        if(fromActivity.equals("job description")) {
            bioTextView.setText(user.getBio());
        } else {
            bioLabel.setVisibility(View.GONE);
            bioTextView.setVisibility(View.GONE);
        }

        if (user.getTotalRating() > 0) {
            userRatingTextView.setText(String.format("%.1f", user.getTotalRating()));
            userRatingBar.setRating((float) user.getTotalRating());
        } else {
            userRatingTextView.setText("No Rating");
            userRatingBar.setVisibility(View.GONE);
        }


        Picasso.get()
                .load(user.getProfileImage())
                .into(userProfileImageView);
    }



    private void initializeViewModel() {
        userInfoWithReviewListViewModel = ViewModelProviders.of(this).get(UserInfoWithReviewListViewModel.class);
        userInfoWithReviewListViewModel.init();
        userInfoWithReviewListViewModel.makeUserReviewApiCall(user.getUserID());
        progressBarLayout.setVisibility(View.VISIBLE);

        userInfoWithReviewListViewModel.getReviewArrayList().observe(this, reviews -> {
            progressBarLayout.setVisibility(View.GONE);

            if(reviews.size() > 0) {
                reviewsRecycleView.setVisibility(View.VISIBLE);
                reviewListAdapter.setReviewArrayList(reviews);
            } else {
                noReviewsTextView.setVisibility(View.VISIBLE);
            }
        });



    }

    private void setToolbarTitle() {
        int userType = Prefs.getInstance().getUserType();
        if(fromActivity.equals("job description")) {
            if (userType == 1) {
                toolbar.setTitle("Tasker Profile");
            } else {
                toolbar.setTitle("Hirer Profile");
            }
        } else {
            toolbar.setTitle("Reviews");
        }
    }
}