package com.wmdd.errandz.login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.Response;
import com.wmdd.errandz.bean.LoginResponse;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginViewModel extends ViewModel {

    public String message;
    private ErrandzApi errandzApi;

    private Prefs sharedPreferences;
    private MutableLiveData<Response> responseMutableLiveData;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        responseMutableLiveData = new MutableLiveData<>();
        sharedPreferences = Prefs.getInstance();
    }


    public void makeLoginCall(String emailID, int loginType, String password) {

        errandzApi.loginRequest(emailID, loginType, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   retrofit2.Response<LoginResponse> loginResponse) {
                if (loginResponse.isSuccessful()) {

                    User user = loginResponse.body().getUser();
                    sharedPreferences.saveUserID(user.getUserID());
                    sharedPreferences.saveEmailID(user.getEmailID());
                    sharedPreferences.saveUserType(user.getUserType());
//                    sharedPreferences.saveDateOfBirth(user.getDateOfBirth());
//                    sharedPreferences.saveProfileImage(user.getProfileImage());
//                    sharedPreferences.saveUserFirstName(user.getFirstName());
//                    sharedPreferences.saveUserLastName(user.getLastName());
//                    sharedPreferences.saveUserBio(user.getBio());

                    responseMutableLiveData.setValue(loginResponse.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

    }

    public MutableLiveData<Response> getLoginResponse() {
        return responseMutableLiveData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();

    }


}
