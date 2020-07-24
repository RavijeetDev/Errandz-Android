package com.wmdd.errandz.hirerJobDescription;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.JobDescription;
import com.wmdd.errandz.bean.JobInfoResponse;
import com.wmdd.errandz.bean.JobListResponse;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HirerJobDescriptionViewModel extends ViewModel {

    private ErrandzApi errandzApi;
    private MutableLiveData<JobDescription> jobDescriptionMutableLiveData;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        jobDescriptionMutableLiveData = new MutableLiveData<>();
    }

    public void makeJobInfoAPiCall(int jobID) {

        errandzApi.hirerJobInfoRequest(jobID)
                .enqueue(new Callback<JobInfoResponse>() {
                    @Override
                    public void onResponse(Call<JobInfoResponse> call, Response<JobInfoResponse> response) {
                        if (response.isSuccessful()) {

                            jobDescriptionMutableLiveData.setValue(response.body().getJobDescription());
                        }
                    }

                    @Override
                    public void onFailure(Call<JobInfoResponse> call, Throwable t) {

                    }
                });
    }

    public MutableLiveData<JobDescription> getJobDescriptionMutableLiveData() {
        return jobDescriptionMutableLiveData;
    }

}
