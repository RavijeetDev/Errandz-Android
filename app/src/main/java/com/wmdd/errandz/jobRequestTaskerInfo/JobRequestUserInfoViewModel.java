package com.wmdd.errandz.jobRequestTaskerInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.JobInfoResponse;
import com.wmdd.errandz.bean.Review;
import com.wmdd.errandz.bean.UserReviewResponse;
import com.wmdd.errandz.data.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobRequestUserInfoViewModel extends ViewModel {

    private Prefs sharedPreference;
    private ErrandzApi errandzApi;

    private MutableLiveData<ArrayList<Review>> reviewArrayList;
    private MutableLiveData<com.wmdd.errandz.bean.Response> jobStatusResponse;

    private String idToken;
    private String uid;

    public void init() {

        sharedPreference = Prefs.getInstance();
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);

        reviewArrayList = new MutableLiveData<>();
        jobStatusResponse = new MutableLiveData<>();

        idToken = sharedPreference.getIDToken();
        uid = sharedPreference.getUID();

    }

    public void makeUserReviewApiCall(int taskerID) {

        errandzApi.userReviewListRequest(idToken, uid, taskerID).enqueue(new Callback<UserReviewResponse>() {
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

    public void callUpdateJobStatusApi(int status, int jobStatusID, int jobID, int taskerID) {

        errandzApi.updateJobStatus(idToken, uid, taskerID, Prefs.getInstance().getUserID(), jobID,
                status, jobStatusID).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    jobStatusResponse.setValue(response.body().getResponse());

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<com.wmdd.errandz.bean.Response> getJobStatusResponse() {
        return jobStatusResponse;
    }
}
