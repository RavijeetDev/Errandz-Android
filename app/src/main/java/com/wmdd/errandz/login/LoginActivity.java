package com.wmdd.errandz.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.wmdd.errandz.R;
import com.wmdd.errandz.address.AddressActivity;
import com.wmdd.errandz.bean.Address;
import com.wmdd.errandz.bean.Response;
import com.wmdd.errandz.hirerHome.HirerHomeActivity;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginViewModel loginViewModel;
    private TextInputLayout emailIDTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private Button loginButton;
    private FrameLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailIDTextInputLayout = findViewById(R.id.email_text_input_layout);
        passwordTextInputLayout = findViewById(R.id.password_text_input_layout);
        loginButton = findViewById(R.id.login_button);
        progressBarLayout = findViewById(R.id.progress_bar_view);

        loginButton.setOnClickListener(this);

        initializeViewModel();


    }

    private void initializeViewModel() {

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.init();

        loginViewModel.getLoginResponse().observe(this, response -> {

            progressBarLayout.setVisibility(View.GONE);

            showResponse(response);

        });

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.login_button) {
            String emailID = emailIDTextInputLayout.getEditText().getText().toString();
            String password = passwordTextInputLayout.getEditText().getText().toString();

            loginViewModel.makeLoginCall(emailID, 1, password);
            progressBarLayout.setVisibility(View.VISIBLE);

        }
    }

    private void showResponse(Response response) {
        if (response.getStatus().equals("success")) {
            callHomeScreen();
        } else {
            // show snackbar
        }
        finish();
    }

    private void callHomeScreen() {
        int userType = Prefs.getInstance().getUserType();
        String address = Prefs.getInstance().getFullAddress();
        if (address.isEmpty()) {
            Intent addressIntent = new Intent(this, AddressActivity.class);
            startActivity(addressIntent);
        } else {
            if (userType == 1) {
                Intent intent = new Intent(this, HirerHomeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), TaskerHomeActivity.class);
                startActivity(intent);
            }
        }
    }
}