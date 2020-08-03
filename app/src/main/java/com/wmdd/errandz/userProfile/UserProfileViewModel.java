package com.wmdd.errandz.userProfile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.bean.UserInfoResponse;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileViewModel extends ViewModel {

    private Prefs sharedPreference;
    private ErrandzApi errandzApi;

    private MutableLiveData<User> userMutableLiveData;

    public void init() {

        sharedPreference = Prefs.getInstance();
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);

        userMutableLiveData = new MutableLiveData<>();

        makeUserInfoApiCall();
    }

    public void makeUserInfoApiCall() {

        int userId = sharedPreference.getUserID();
        String idToken = sharedPreference.getIDToken();
        String uid = sharedPreference.getUID();

        errandzApi.userInfoRequest(idToken, uid, userId).enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if(response.isSuccessful()) {
                    userMutableLiveData.setValue(response.body().getUser());
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<User> getUserInfo() {
        return userMutableLiveData;
    }
}
