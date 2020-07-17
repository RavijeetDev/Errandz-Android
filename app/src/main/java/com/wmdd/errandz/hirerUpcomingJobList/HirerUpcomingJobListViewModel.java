package com.wmdd.errandz.hirerUpcomingJobList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.HirerUpcomingJobListResponse;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.data.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HirerUpcomingJobListViewModel extends ViewModel {

    private ErrandzApi errandzApi;

    private MutableLiveData<ArrayList<Job>> jobArrayList;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        jobArrayList = new MutableLiveData<>();
        makeUpcomingJobCall();
    }

    public void makeUpcomingJobCall() {

        int userID = Prefs.getInstance().getUserID();

        errandzApi.hirerUpcomingJobListRequest(userID)
                .enqueue(new Callback<HirerUpcomingJobListResponse>() {
                    @Override
                    public void onResponse(Call<HirerUpcomingJobListResponse> call, Response<HirerUpcomingJobListResponse> response) {
                        if (response.isSuccessful() && response.body().getResponse().getStatus().equals("success")) {
                            jobArrayList.setValue(response.body().getJobArrayList());
                        }
                    }

                    @Override
                    public void onFailure(Call<HirerUpcomingJobListResponse> call, Throwable t) {

                    }
                });
    }

    public MutableLiveData<ArrayList<Job>> getJobArrayList() {
        return jobArrayList;
    }
}
