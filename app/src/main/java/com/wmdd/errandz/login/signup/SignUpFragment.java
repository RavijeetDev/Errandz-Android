package com.wmdd.errandz.login.signup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.wmdd.errandz.R;
import com.wmdd.errandz.databinding.FragmentSignUpBinding;
import com.wmdd.errandz.util.Utility;

public class SignUpFragment extends Fragment implements View.OnClickListener{

    private final static int NO_USER_TYPE = 0;
    private final static int HIRER_USER_TYPE = 1;
    private final static int TASKER_USER_TYPE = 2;

    private final static int EMAIL_PASSWORD_LOGIN = 1;

    private Snackbar snackbar;

    private int userType = NO_USER_TYPE;

    private long dateOFBirthTimestamp = 0;

    private SignUpViewModel signUpViewModel;
    private FragmentSignUpBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sign_up,
                container,
                false);



        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        signUpViewModel.init();

        binding.signUpButton.setOnClickListener(this);

        binding.hiringRadioButton.setOnClickListener(this);
        binding.jobSeekerRadioButton.setOnClickListener(this);

        binding.birthDateTextInputLayout.setOnClickListener(this);
        binding.birthDateEditText.setOnClickListener(this);

        addTextChangeListener(binding.firstNameTextInputLayout);
        addTextChangeListener(binding.lastNameTextInputLayout);
        addTextChangeListener(binding.emailTextInputLayout);
        addTextChangeListener(binding.passwordTextInputLayout);
        addTextChangeListener(binding.confirmPasswordTextInputLayout);
        addTextChangeListener(binding.birthDateTextInputLayout);

        initializeViewModelResponses();

        binding.signUpToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return binding.getRoot();
    }

    private void initializeViewModelResponses() {

        signUpViewModel.getSignUpResponse().observe(this, response -> {

            binding.progressBarView.setVisibility(View.GONE);
            if(response.getStatus().equals("success")) {
                showToast("Signed Up Successfully");
                getActivity().onBackPressed();
            } else {
                showSnackbar(response.getMessage());
            }

        });

        signUpViewModel.getErrorResponse().observe(this, error -> {

            binding.progressBarView.setVisibility(View.GONE);
            showSnackbar(error);
        });

    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_up_button:
                signUp();
                break;
            case R.id.hiring_radio_button:
                userType = HIRER_USER_TYPE;
                break;
            case R.id.job_seeker_radio_button:
                userType = TASKER_USER_TYPE;
                break;
            case R.id.birth_date_edit_text:
            case R.id.birth_date_text_input_layout:
                MaterialDatePicker.Builder<Long> datePickerBuilder = MaterialDatePicker.Builder.datePicker();
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                try {
                    datePickerBuilder.setCalendarConstraints(constraintsBuilder.build());
                    MaterialDatePicker<?> picker = datePickerBuilder.build();

                    picker.show(getChildFragmentManager(), picker.toString());
                    addPickerListeners(picker);
                } catch (IllegalArgumentException e) {
                    Log.e("message", e.getMessage());
                }
                break;
        }
    }





    private void addPickerListeners(MaterialDatePicker<?> materialDatePicker) {
        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> {
                    binding.birthDateEditText.setText(materialDatePicker.getHeaderText());
                    Object timestampObject = materialDatePicker.getSelection();
                    dateOFBirthTimestamp = (long) timestampObject;
                });
    }

    private void signUp() {

        String firstName = binding.firstNameEditText.getText().toString();
        String lastName = binding.lastNameEditText.getText().toString();
        String emailID = binding.emailIdEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();
        String confirmPassword = binding.confirmPasswordEditText.getText().toString();

        if(firstName.isEmpty()) {
            binding.firstNameTextInputLayout.setError(getString(R.string.empty_first_name));
        } else if(!Utility.isNameValid(firstName)){
            binding.firstNameTextInputLayout.setError(getString(R.string.first_name_invalid));
        } else if(lastName.isEmpty()) {
            binding.lastNameTextInputLayout.setError(getString(R.string.empty_last_name));
        } else if(!Utility.isNameValid(lastName)) {
            binding.lastNameTextInputLayout.setError(getString(R.string.last_name_invalid));
        } else if(emailID.isEmpty()) {
            binding.emailTextInputLayout.setError(getString(R.string.empty_email));
        } else if(!Utility.isEmailValid(emailID)) {
            binding.emailTextInputLayout.setError(getString(R.string.email_invalid));
        } else if(password.isEmpty()) {
            binding.passwordTextInputLayout.setError(getString(R.string.empty_password));
        }else if(password.length() < 6) {
            binding.passwordTextInputLayout.setError(getString(R.string.password_min_length));
        }else if(confirmPassword.isEmpty()) {
            binding.confirmPasswordTextInputLayout.setError(getString(R.string.empty_password));
        } else if(!password.equals(confirmPassword)) {
            binding.passwordTextInputLayout.setError(getString(R.string.password_confirm_password_not_matched));
            binding.confirmPasswordTextInputLayout.setError(getString(R.string.password_confirm_password_not_matched));
        } else if(dateOFBirthTimestamp == 0) {
            binding.birthDateTextInputLayout.setError(getString(R.string.birth_date_empty));
        } else if(Utility.getAge(dateOFBirthTimestamp) < 14){
            binding.birthDateTextInputLayout.setError(getString(R.string.invalid_age));
        } else if(userType == 0){

            showSnackbar("Please select your profile type!");

        } else {

            binding.progressBarView.setVisibility(View.VISIBLE);
            signUpViewModel.signUpRequest(firstName, lastName, emailID, password, dateOFBirthTimestamp,
                    userType, EMAIL_PASSWORD_LOGIN, ((AppCompatActivity)getActivity()));
        }

    }

    private void addTextChangeListener(final TextInputLayout textInputLayout) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (textInputLayout.getError() != null) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }


    private void showSnackbar(String message) {

        if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
        snackbar = Snackbar.make(binding.confirmPasswordTextInputLayout, message,
                Snackbar.LENGTH_SHORT);
        Utility.initializeSnackBar(snackbar, getContext());
        snackbar.show();
    }


}