package com.wmdd.errandz.userProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.SettingsActivity;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.hirerHome.HirerHomeActivity;
import com.wmdd.errandz.login.LoginActivity;
import com.wmdd.errandz.userInfoWithReviewList.UserInfoWithReviewListActivity;
import com.wmdd.errandz.userProfileEdit.UserProfileEditActivity;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeActivity;

public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private ImageView profileImageView;
    private TextView userFullNameTextView;
    private TextView userEmailTextView;
    private TextView userRatingTextView;
    private RatingBar userRatingBar;
    private TextView userBioTextView;
    private TextView userBioLabel;
    private Button reviewButton;
    private Button settingsButton;
    private Button logoutButton;
    private MaterialToolbar toolbar;
    private User user;

    private Menu menu;

    private UserProfileViewModel userProfileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        profileImageView = rootView.findViewById(R.id.user_profile_image_view);
        userFullNameTextView = rootView.findViewById(R.id.user_name_text_view);
        userEmailTextView = rootView.findViewById(R.id.user_email_text_view);
        userRatingTextView = rootView.findViewById(R.id.user_rating_text_view);
        userRatingBar = rootView.findViewById(R.id.user_rating_bar);
        userBioLabel = rootView.findViewById(R.id.user_bio_label);
        userBioTextView = rootView.findViewById(R.id.user_bio_text_view);
        reviewButton = rootView.findViewById(R.id.review_button);
        settingsButton = rootView.findViewById(R.id.settings_button);
        logoutButton = rootView.findViewById(R.id.logout_button);

        if (getActivity() instanceof HirerHomeActivity) {
            toolbar = ((HirerHomeActivity) getActivity()).findViewById(R.id.toolbar);
        } else {
            toolbar = ((TaskerHomeActivity) getActivity()).findViewById(R.id.toolbar);
        }
        menu = toolbar.getMenu();

        reviewButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        setMenuItemClickListener();


        return rootView;
    }

    private void setMenuItemClickListener() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.user_edit_menu) {
                    Intent intent = new Intent(getActivity(), UserProfileEditActivity.class);
                    intent.putExtra("USER", user);
                    startActivity(intent);
                }
                return false;
            }
        });

    }

    private void initializeViewModel() {
        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        userProfileViewModel.init();

        userProfileViewModel.getUserInfo().observe(this, userInfo -> {

            userFullNameTextView.setText(userInfo.getFirstName() + " " + userInfo.getLastName());
            userEmailTextView.setText(userInfo.getEmailID());

            if (userInfo.getTotalRating() > 0) {
                userRatingTextView.setText(String.format("%.1f", userInfo.getTotalRating()));
                userRatingBar.setRating((float) userInfo.getTotalRating());
                userRatingBar.setVisibility(View.VISIBLE);
            } else {
                userRatingTextView.setText("No Rating");
            }
            user = userInfo;
            if (userInfo.getBio().isEmpty()) {
                userBioLabel.setVisibility(View.GONE);
            } else {
                userBioLabel.setVisibility(View.VISIBLE);
                userBioTextView.setText(userInfo.getBio());
            }

            Picasso.get()
                    .load(userInfo.getProfileImage())
                    .into(profileImageView);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeViewModel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_button:

                Prefs sharedPreferences = Prefs.getInstance();

                sharedPreferences.saveUserID(0);
                sharedPreferences.saveEmailID("");
                sharedPreferences.saveUserType(0);
                sharedPreferences.saveFullAddress("");

                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(loginIntent);
                getActivity().finish();

                break;
            case R.id.review_button:
                Intent reviewIntent = new Intent(getContext(), UserInfoWithReviewListActivity.class);
                reviewIntent.putExtra("FROM_ACTIVITY", "user profile");
                reviewIntent.putExtra("USER", user);
                startActivity(reviewIntent);
                break;
            case R.id.settings_button:
                Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
        }
    }
}
