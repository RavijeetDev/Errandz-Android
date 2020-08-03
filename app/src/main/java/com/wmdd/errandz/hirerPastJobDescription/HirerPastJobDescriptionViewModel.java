package com.wmdd.errandz.hirerPastJobDescription;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.JobInfoResponse;
import com.wmdd.errandz.bean.Review;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.bean.UserReviewResponse;
import com.wmdd.errandz.data.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HirerPastJobDescriptionViewModel extends ViewModel {

    private Prefs sharedPreference;
    private ErrandzApi errandzApi;

    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Job> jobMutableLiveData;
    private MutableLiveData<ArrayList<Review>> reviewList;
    private MutableLiveData<com.wmdd.errandz.bean.Response> newReviewAddedResponse;

    private String idToken;
    private String uid;

    public void init() {

        sharedPreference = Prefs.getInstance();
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);

        jobMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
        reviewList = new MutableLiveData<>();
        newReviewAddedResponse = new MutableLiveData<>();

        idToken = sharedPreference.getIDToken();
        uid = sharedPreference.getUID();
    }

    public void makeHistoryJobInfoApiCall(int taskerID, int jobID) {

        int userID = sharedPreference.getUserID();

        errandzApi.hirerJobInfoHistoryRequest(idToken, uid, jobID, userID, taskerID).enqueue(new Callback<JobInfoResponse>() {
            @Override
            public void onResponse(Call<JobInfoResponse> call, Response<JobInfoResponse> response) {
                if (response.isSuccessful()) {
                    jobMutableLiveData.setValue(response.body().getJobDescription().getJob());
                    userMutableLiveData.setValue(response.body().getJobDescription().getUser());
                }
            }

            @Override
            public void onFailure(Call<JobInfoResponse> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });

    }

    public void makeJobReviewApiCall(int jobID) {


        errandzApi.jobReviewListCall(idToken, uid, jobID, Prefs.getInstance().getUserID())
                .enqueue(new Callback<UserReviewResponse>() {
            @Override
            public void onResponse(Call<UserReviewResponse> call, Response<UserReviewResponse> response) {
                if (response.isSuccessful()) {
                    reviewList.setValue(response.body().getReviewArrayList());
                }
            }

            @Override
            public void onFailure(Call<UserReviewResponse> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });

    }

    public void makeNewReviewCall(int jobID, int taskerID, float rating, String review) {

        errandzApi.addNewReviewRequest(idToken, uid, jobID, taskerID, Prefs.getInstance()
                .getUserID(), rating, review).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    newReviewAddedResponse.setValue(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
    }

    public MutableLiveData<Job> getJobMutableLiveData() {
        return jobMutableLiveData;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<ArrayList<Review>> getReviewList() {
        return reviewList;
    }

    public MutableLiveData<com.wmdd.errandz.bean.Response> getNewReviewAddedResponse() {
        return newReviewAddedResponse;
    }


}
