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
import com.wmdd.errandz.databinding.ActivityLoginBinding;
import com.wmdd.errandz.hirerHome.HirerHomeActivity;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.login.signup.SignUpFragment;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeActivity;

public class LoginActivity extends AppCompatActivity implements AddMoreInfoFragment.CallbackListener {

    private LoginViewModel loginViewModel;
    private TextInputLayout emailIDTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private Button loginButton;
    private FrameLayout progressBarLayout;

    private ActivityLoginBinding activityLoginBinding;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFragment = new LoginFragment();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, loginFragment)
                    .commitAllowingStateLoss();
        }



    }

    @Override
    public void onAddingMoreInfo(long dateOfBirthTimestamp, int userType) {
        loginFragment.onAddingMoreInfo(dateOfBirthTimestamp, userType);
    }
}