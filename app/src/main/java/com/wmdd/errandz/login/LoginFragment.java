package com.wmdd.errandz.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.wmdd.errandz.R;
import com.wmdd.errandz.address.AddressActivity;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.databinding.FragmentLoginBinding;
import com.wmdd.errandz.hirerHome.HirerHomeActivity;
import com.wmdd.errandz.login.signup.SignUpFragment;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeActivity;
import com.wmdd.errandz.userProfileEdit.UserProfileEditActivity;
import com.wmdd.errandz.util.Constants;
import com.wmdd.errandz.util.Utility;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private final static int EMAIL_PASSWORD_LOGIN_TYPE = 1;
    private static final int RC_SIGN_IN = 1;

    private final static String FROM_ACTIVITY = "from_activity";

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;

    private Snackbar snackbar;
    private GoogleSignInClient googleSignInClient;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String emailID;
    private String idToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login,
                container,
                false);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.init();

        binding.loginButton.setOnClickListener(this);
        binding.googleLoginButton.setOnClickListener(this);
        binding.signUpLink.setOnClickListener(this);

        addTextChangeListener(binding.emailTextInputLayout);
        addTextChangeListener(binding.passwordTextInputLayout);

        initializeViewModelResponses();
        initializeGoogleSignIn();

        return binding.getRoot();
    }

    private void initializeGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.server_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                login();
                break;
            case R.id.google_login_button:
                googleSignUp();
                break;
            case R.id.sign_up_link:
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new SignUpFragment())
                        .addToBackStack(SignUpFragment.class.getSimpleName())
                        .commitAllowingStateLoss();
                break;
        }
    }

    private void initializeViewModelResponses() {

        loginViewModel.getLoginResponse().observe(this, response -> {

            binding.progressBarView.setVisibility(View.GONE);
            if(response.getResponse().getStatus().equals("success")) {


                Prefs sharedPreference = Prefs.getInstance();

                if(sharedPreference.getFullAddress().isEmpty()) {
                    openAddressActivity(response.getUser());
                } else {
                    openHomeScreen();
                }

                getActivity().finish();

            } else {
                showSnackbar(response.getResponse().getMessage());
            }

        });

        loginViewModel.getErrorResponse().observe(this, error -> {

            if(error.equals("Google not registered")) {

                getFragmentManager().beginTransaction()
                        .add(R.id.container, new AddMoreInfoFragment())
                        .addToBackStack(AddMoreInfoFragment.class.getSimpleName())
                        .commitAllowingStateLoss();
            } else {
                binding.progressBarView.setVisibility(View.GONE);
                showSnackbar(error);
            }
        });

    }

    private void googleSignUp() {
        binding.progressBarView.setVisibility(View.VISIBLE);
        GoogleSignInAccount alreadyLoggedAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (alreadyLoggedAccount != null) {
            Log.d("Google", "Already Logged In");
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent signInIntent = googleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
        } else {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }


    private void login() {
        String emailID = binding.emailTextInputEditText.getText().toString();
        String password = binding.passwordTextInputEditText.getText().toString();

        if(emailID.isEmpty()) {
            binding.emailTextInputLayout.setError(getString(R.string.empty_email));
        } else if(!Utility.isEmailValid(emailID)) {
            binding.emailTextInputLayout.setError(getString(R.string.email_invalid));
        } else if(password.isEmpty()) {
            binding.passwordTextInputLayout.setError(getString(R.string.empty_password));
        } else {
            binding.progressBarView.setVisibility(View.VISIBLE);
            loginViewModel.makeFirebaseLoginCall(emailID, password, EMAIL_PASSWORD_LOGIN_TYPE, (AppCompatActivity)getActivity());
        }
    }

    private void showSnackbar(String message) {

        if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
        snackbar = Snackbar.make(binding.passwordTextInputLayout, message,
                Snackbar.LENGTH_SHORT);
        Utility.initializeSnackBar(snackbar, getContext());
        snackbar.show();
    }

    private void openAddressActivity(User user) {
        Intent intent = new Intent(getActivity(), AddressActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    private void openUserEditProfile(User user) {
        Intent intent = new Intent(getActivity(), UserProfileEditActivity.class);
        intent.putExtra(FROM_ACTIVITY, LoginFragment.class.getSimpleName());
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    private void openHomeScreen() {
        int userType = Prefs.getInstance().getUserType();

        if(userType == Constants.HIRER) {
            Intent intent = new Intent(getActivity(), HirerHomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), TaskerHomeActivity.class);
            startActivity(intent);
        }
    }

        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RC_SIGN_IN:
                    try {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firstName = account.getGivenName();
                        lastName = account.getFamilyName();
//                        uid = account.getId();
                        profileImage = account.getPhotoUrl().toString();
                        emailID = account.getEmail();
                        idToken = account.getIdToken();


                        loginViewModel.loginWithGoogle(emailID, idToken, ((AppCompatActivity)getActivity()));
                        Log.d("Google", account.toString());

                    } catch (ApiException e) {
                        showSnackbar("Error in fetching account detail !");
                    }
                    break;
            }
        } else {
            binding.progressBarView.setVisibility(View.GONE);
            if (requestCode == RC_SIGN_IN) {
                showSnackbar("Could not login with google !");
            }
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


    public void onAddingMoreInfo(long dateOfBirthTimestamp, int userType) {
        binding.progressBarView.setVisibility(View.VISIBLE);
        loginViewModel.makeGoogleLogin(firstName, lastName, dateOfBirthTimestamp, userType,
                profileImage);
    }
}