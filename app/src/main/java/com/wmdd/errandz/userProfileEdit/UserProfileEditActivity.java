package com.wmdd.errandz.userProfileEdit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.userProfile.UserProfileViewModel;

public class UserProfileEditActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private UserProfileEditViewModel userProfileEditViewModel;

    private ImageView profileImageView;
    private TextInputLayout firstNameTextInputLayout;
    private TextInputLayout lastNameTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout dobTextInputLayout;
    private TextInputLayout addressTextInputLayout;
    private TextInputLayout bioTextInputLayout;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);

        user = getIntent().getParcelableExtra("USER");

        profileImageView = findViewById(R.id.user_profile_image_view);
        firstNameTextInputLayout = findViewById(R.id.first_name_text_input_layout);
        lastNameTextInputLayout = findViewById(R.id.last_name_text_input_layout);
        emailTextInputLayout = findViewById(R.id.email_text_input_layout);
        dobTextInputLayout = findViewById(R.id.dob_text_input_layout);
        addressTextInputLayout = findViewById(R.id.address_text_input_layout);
        bioTextInputLayout = findViewById(R.id.bio_text_input_layout);
        updateButton = findViewById(R.id.update_button);

        updateButton.setOnClickListener(this);

        emailTextInputLayout.getEditText().setEnabled(false);
        dobTextInputLayout.getEditText().setEnabled(false);

        setValuesInLayouts();
        initializeViewModel();
    }

    private void setValuesInLayouts() {
        firstNameTextInputLayout.getEditText().setText(user.getFirstName());
        lastNameTextInputLayout.getEditText().setText(user.getLastName());
        emailTextInputLayout.getEditText().setText(user.getEmailID());
        dobTextInputLayout.getEditText().setText(user.getDateOfBirth());
//        addressTextInputLayout.getEditText().setText();
        bioTextInputLayout.getEditText().setText(user.getBio());

        Picasso.get()
                .load(user.getProfileImage())
                .into(profileImageView);
    }

    private void initializeViewModel() {
        userProfileEditViewModel = ViewModelProviders.of(this).get(UserProfileEditViewModel.class);
        userProfileEditViewModel.init();

        userProfileEditViewModel.getResponse().observe(this, response -> {

//            progressBarLayout.setVisibility(View.GONE);

            if(response.getStatus().equals("success")) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.update_button) {
            String firstName = firstNameTextInputLayout.getEditText().getText().toString();
            String lastName = lastNameTextInputLayout.getEditText().getText().toString();
            String bio = bioTextInputLayout.getEditText().getText().toString();

            userProfileEditViewModel.updateUserProfileApiCall(firstName, lastName, bio);
        }
    }
}