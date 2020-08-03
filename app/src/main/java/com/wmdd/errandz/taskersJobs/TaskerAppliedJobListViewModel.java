package com.wmdd.errandz.taskersJobs;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.Job;
import com.wmdd.errandz.bean.JobListResponse;
import com.wmdd.errandz.bean.TaskerHomeData;
import com.wmdd.errandz.bean.TaskerHomeDataResponse;
import com.wmdd.errandz.data.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskerAppliedJobListViewModel extends ViewModel {

    private Prefs sharedPreference;
    private ErrandzApi errandzApi;

    private MutableLiveData<ArrayList<Job>> appliedJobListMutableLiveData;

    public void init() {

        sharedPreference = Prefs.getInstance();
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);

        appliedJobListMutableLiveData = new MutableLiveData<>();

        makeTaskerAppliedJobApiCall();

    }

    public void makeTaskerAppliedJobApiCall() {

        int userId = sharedPreference.getUserID();
        String idToken = sharedPreference.getIDToken();
        String uid = sharedPreference.getUID();

        errandzApi.taskerAppliedJobListRequest(idToken, uid, userId).enqueue(new Callback<JobListResponse>() {
            @Override
            public void onResponse(Call<JobListResponse> call, Response<JobListResponse> response) {
                if(response.isSuccessful()) {
                   appliedJobListMutableLiveData.setValue(response.body().getJobArrayList());
                }
            }

            @Override
            public void onFailure(Call<JobListResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<ArrayList<Job>> getAppliedJobListMutableLiveData() {
        return appliedJobListMutableLiveData;
    }

}
