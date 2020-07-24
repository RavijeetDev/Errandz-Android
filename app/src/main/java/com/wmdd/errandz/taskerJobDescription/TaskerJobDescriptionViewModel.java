package com.wmdd.errandz.taskerJobDescription;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.JobInfoResponse;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.bean.UserInfoResponse;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskerJobDescriptionViewModel extends ViewModel {

    private ErrandzApi errandzApi;
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Job> jobMutableLiveData;
    private MutableLiveData<com.wmdd.errandz.bean.Response> responseMutableLiveData;
    private MutableLiveData<com.wmdd.errandz.bean.Response> savedMutableLiveData;
    private MutableLiveData<com.wmdd.errandz.bean.Response> jobStartedMutableLiveData;
    private MutableLiveData<com.wmdd.errandz.bean.Response> jobCompletedMutableLiveData;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        jobMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
        responseMutableLiveData = new MutableLiveData<>();
        savedMutableLiveData = new MutableLiveData<>();
        jobStartedMutableLiveData = new MutableLiveData<>();
        jobCompletedMutableLiveData = new MutableLiveData<>();
    }

    public void makeJobInfoApiCall(int hirerId, int jobID) {

        int userID = Prefs.getInstance().getUserID();
        errandzApi.taskerJobInfoRequest(jobID, hirerId, userID).enqueue(new Callback<JobInfoResponse>() {
            @Override
            public void onResponse(Call<JobInfoResponse> call, Response<JobInfoResponse> response) {
                if (response.isSuccessful()) {
                    userMutableLiveData.setValue(response.body().getJobDescription().getUser());
                    jobMutableLiveData.setValue(response.body().getJobDescription().getJob());
                }
            }

            @Override
            public void onFailure(Call<JobInfoResponse> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });

    }

    public void makeApplyJobApiCall(Job job) {

        int userID = Prefs.getInstance().getUserID();
        errandzApi.updateJobStatus(userID, job.getHirerID(), job.getJobID(), 1, job.getJobStatusID()).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    responseMutableLiveData.setValue(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });

    }

    public void saveJob(Job job) {

        int userId = Prefs.getInstance().getUserID();

        errandzApi.updateJobStatus(userId, job.getHirerID(), job.getJobID(), 4, job.getJobStatusID()).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    savedMutableLiveData.setValue(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public void unsaveJob(Job job) {

        errandzApi.unsaveJob(job.getJobStatusID()).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    savedMutableLiveData.setValue(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public void setJobStatusStartedApiCall(int hirerID, int jobID, int jobStatusID) {

        errandzApi.updateJobStatus(Prefs.getInstance().getUserID(), hirerID, jobID, 5, jobStatusID).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    jobStartedMutableLiveData.setValue(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public void setCompleteStatusOFJob(int hirerID, int jobID, int jobStatusID) {
        errandzApi.updateJobStatus(Prefs.getInstance().getUserID(), hirerID, jobID, 6, jobStatusID).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    jobCompletedMutableLiveData.setValue(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Job> getJobMutableLiveData() {
        return jobMutableLiveData;
    }

    public MutableLiveData<com.wmdd.errandz.bean.Response> getResponseMutableLiveData() { return responseMutableLiveData; }

    public MutableLiveData<com.wmdd.errandz.bean.Response> getSavedMutableLiveData() { return savedMutableLiveData; }

    public MutableLiveData<com.wmdd.errandz.bean.Response> getJobStartedMutableLiveData() { return jobStartedMutableLiveData; }

    public MutableLiveData<com.wmdd.errandz.bean.Response> getJobCompletedMutableLiveData() {
        return jobCompletedMutableLiveData;
    }
}
