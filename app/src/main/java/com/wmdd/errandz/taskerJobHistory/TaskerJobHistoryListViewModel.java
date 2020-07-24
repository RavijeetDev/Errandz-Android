package com.wmdd.errandz.taskerJobHistory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.JobListResponse;
import com.wmdd.errandz.data.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskerJobHistoryListViewModel extends ViewModel {
    private ErrandzApi errandzApi;

    private MutableLiveData<ArrayList<Job>> jobArrayList;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        jobArrayList = new MutableLiveData<>();
        makeUpcomingJobCall();
    }

    public void makeUpcomingJobCall() {

        int userID = Prefs.getInstance().getUserID();

        errandzApi.taskerJobHistoryListRequest(userID)
                .enqueue(new Callback<JobListResponse>() {
                    @Override
                    public void onResponse(Call<JobListResponse> call, Response<JobListResponse> response) {
                        if (response.isSuccessful() && response.body().getResponse().getStatus().equals("success")) {
                            jobArrayList.setValue(response.body().getJobArrayList());
                        }
                    }

                    @Override
                    public void onFailure(Call<JobListResponse> call, Throwable t) {

                    }
                });
    }

    public MutableLiveData<ArrayList<Job>> getJobArrayList() {
        return jobArrayList;
    }
}
