package com.wmdd.errandz.jobRequestList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.HirerHomeData;
import com.wmdd.errandz.bean.HirerHomeResponse;
import com.wmdd.errandz.bean.JobDescription;
import com.wmdd.errandz.bean.JobRequestListResponse;
import com.wmdd.errandz.data.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobRequestListViewModel extends ViewModel {

    private ErrandzApi errandzApi;
    private MutableLiveData<ArrayList<JobDescription>> jobArrayList;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        jobArrayList = new MutableLiveData<>();
        makeHirerHomeDataApiCall();
    }


    public void makeHirerHomeDataApiCall() {

        int userID = Prefs.getInstance().getUserID();

        errandzApi.jobRequestListRequest(userID).enqueue(new Callback<JobRequestListResponse>() {
            @Override
            public void onResponse(Call<JobRequestListResponse> call, Response<JobRequestListResponse> response) {
                if(response.isSuccessful()) {
                    jobArrayList.setValue(response.body().getJobDescriptionArrayList());
                }
            }

            @Override
            public void onFailure(Call<JobRequestListResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<ArrayList<JobDescription>> getJobArrayList() {
        return jobArrayList;
    }

}
