package com.wmdd.errandz.taskerHomeScreen;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.TaskerHomeData;
import com.wmdd.errandz.bean.TaskerHomeDataResponse;
import com.wmdd.errandz.data.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskerHomeViewModel extends ViewModel {

    private ErrandzApi errandzApi;
    private MutableLiveData<ArrayList<Job>> approvedJobArrayList;
    private MutableLiveData<ArrayList<Job>> upcomingJobArrayList;
    private MutableLiveData<com.wmdd.errandz.bean.Response> savedResponse;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        approvedJobArrayList = new MutableLiveData<>();
        upcomingJobArrayList = new MutableLiveData<>();
        savedResponse = new MutableLiveData<>();

    }

    public void makeClientHomeDataApiCall() {

        int userId = Prefs.getInstance().getUserID();

        errandzApi.taskerHomeDataRequest(userId).enqueue(new Callback<TaskerHomeDataResponse>() {
            @Override
            public void onResponse(Call<TaskerHomeDataResponse> call, Response<TaskerHomeDataResponse> response) {
                if(response.isSuccessful()) {
                    TaskerHomeData taskerHomeData = response.body().getTaskerHomeData();
                    approvedJobArrayList.setValue(taskerHomeData.getApprovedJobArrayList());
                    upcomingJobArrayList.setValue(taskerHomeData.getNearbyJobArrayList());
                }
            }

            @Override
            public void onFailure(Call<TaskerHomeDataResponse> call, Throwable t) {

            }
        });
    }

    public void saveJob(Job job) {

        int userId = Prefs.getInstance().getUserID();

        errandzApi.updateJobStatus(userId, job.getHirerID(), job.getJobID(), 4, job.getJobStatusID()).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                   savedResponse.setValue(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public void unsaveJob(Job job) {

        int userId = Prefs.getInstance().getUserID();

        errandzApi.unsaveJob(job.getJobStatusID()).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    savedResponse.setValue(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<ArrayList<Job>> getApprovedJobList() {
        return approvedJobArrayList;
    }

    public MutableLiveData<ArrayList<Job>> getUpcomingJobList() {
        return upcomingJobArrayList;
    }

    public MutableLiveData<com.wmdd.errandz.bean.Response> getSavedResponse() {
        return savedResponse;
    }


}
