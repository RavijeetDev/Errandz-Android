package com.wmdd.errandz.userInfoWithReviewList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.JobInfoResponse;
import com.wmdd.errandz.bean.Review;
import com.wmdd.errandz.bean.UserReviewResponse;
import com.wmdd.errandz.data.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoWithReviewListViewModel extends ViewModel {

    private Prefs sharedPreference;
    private ErrandzApi errandzApi;

    private MutableLiveData<ArrayList<Review>> reviewArrayList;

    public void init() {

        sharedPreference = Prefs.getInstance();
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);

        reviewArrayList = new MutableLiveData<>();

    }

    public void makeUserReviewApiCall(int hirerId) {

        String idToken = sharedPreference.getIDToken();
        String uid = sharedPreference.getUID();

        errandzApi.userReviewListRequest(idToken, uid, hirerId).enqueue(new Callback<UserReviewResponse>() {
            @Override
            public void onResponse(Call<UserReviewResponse> call, Response<UserReviewResponse> response) {
                if (response.isSuccessful()) {
                    reviewArrayList.setValue(response.body().getReviewArrayList());

                }
            }

            @Override
            public void onFailure(Call<UserReviewResponse> call, Throwable t) {

            }
        });

    }


    public MutableLiveData<ArrayList<Review>> getReviewArrayList() {
        return reviewArrayList;
    }
}
