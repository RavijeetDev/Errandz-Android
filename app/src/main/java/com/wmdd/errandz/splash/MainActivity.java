package com.wmdd.errandz.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wmdd.errandz.R;
import com.wmdd.errandz.address.AddressActivity;
import com.wmdd.errandz.bean.Address;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.hirerHome.HirerHomeActivity;
import com.wmdd.errandz.login.LoginActivity;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginStatus();
            }
        }, 3000);
    }

    private void checkLoginStatus() {
        if(Prefs.getInstance().getUserID() > 0) {
            checkUserType();
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private void checkUserType() {
        String address = Prefs.getInstance().getFullAddress();
        if (address.isEmpty()) {
            Intent addressIntent = new Intent(this, AddressActivity.class);
            startActivity(addressIntent);
        } else {
            if (Prefs.getInstance().getUserType() == 1) {
                Intent intent = new Intent(getApplicationContext(), HirerHomeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), TaskerHomeActivity.class);
                startActivity(intent);
            }
        }
    }
}