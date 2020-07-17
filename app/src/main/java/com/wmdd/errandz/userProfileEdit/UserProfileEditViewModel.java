package com.wmdd.errandz.userProfileEdit;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.Response;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;

public class UserProfileEditViewModel extends ViewModel {

    private ErrandzApi errandzApi;

    private MutableLiveData<Response> updateProfileResponse;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        updateProfileResponse = new MutableLiveData<>();
    }

    public void updateUserProfileApiCall(String firstName, String lastName, String bio) {

        int userID = Prefs.getInstance().getUserID();

        errandzApi.uploadUserInfoRequest(userID, firstName, lastName, bio)
                .enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            updateProfileResponse.setValue(response.body().getResponse());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
    }

    public MutableLiveData<Response> getResponse() {
        return updateProfileResponse;
    }
}
