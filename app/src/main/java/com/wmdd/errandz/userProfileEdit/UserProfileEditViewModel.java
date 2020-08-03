package com.wmdd.errandz.userProfileEdit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.Address;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.Response;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.bean.UserInfoResponse;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;

public class UserProfileEditViewModel extends ViewModel {

    private static final int UPDATE_USER_PROFILE_API = 1;
    private static final int GET_USER_INFO_API = 2;

    private Prefs sharedPreference;
    private ErrandzApi errandzApi;

    private MutableLiveData<Response> updateProfileResponse;
    private MutableLiveData<User> userMutableLiveData;

    private int userId;
    private String idToken;
    private String uid;

    private String firstName;
    private String lastName;
    private String bio;
    private String addressJsonString;

    public void init() {

        sharedPreference = Prefs.getInstance();
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);

        updateProfileResponse = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();

        userId = sharedPreference.getUserID();
        idToken = sharedPreference.getIDToken();
        uid = sharedPreference.getUID();

    }

    public void updateUserProfileApiCall(String firstName, String lastName, String bio, String addressJsonString) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.addressJsonString = addressJsonString;

        errandzApi.uploadUserInfoRequest(idToken, uid, userId, firstName, lastName, bio, addressJsonString)
                .enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                        if (response.isSuccessful()) {

                            String status = response.body().getResponse().getStatus();
                            String message = response.body().getResponse().getMessage();

                            if (status.equals("success")) {
                                updateProfileResponse.setValue(response.body().getResponse());
                            } else if (status.equals("404") && message.equals("Token expired")) {
                                getNewToken(UPDATE_USER_PROFILE_API);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
    }

    public void getUserInfo() {

        errandzApi.userInfoRequest(idToken, uid, userId)
                .enqueue(new Callback<UserInfoResponse>() {
                    @Override
                    public void onResponse(Call<UserInfoResponse> call, retrofit2.Response<UserInfoResponse> response) {
                        if (response.isSuccessful()) {

                            String status = response.body().getResponse().getStatus();
                            String message = response.body().getResponse().getMessage();

                            if (status.equals("success")) {
                                userMutableLiveData.setValue(response.body().getUser());
                            } else if (status.equals("404") && message.equals("Token expired")) {
                                getNewToken(GET_USER_INFO_API);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }

    public MutableLiveData<Response> getResponse() {
        return updateProfileResponse;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    private void getNewToken(int apiCall) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            idToken = task.getResult().getToken();
                            sharedPreference.saveIDToken(idToken);

                            if (apiCall == UPDATE_USER_PROFILE_API) {
                                updateUserProfileApiCall(firstName, lastName, bio, addressJsonString);
                            } else {
                                getUserInfo();
                            }
                        } else {
                            Log.e("Token", "token issue");
                        }
                    }
                });
    }
}
