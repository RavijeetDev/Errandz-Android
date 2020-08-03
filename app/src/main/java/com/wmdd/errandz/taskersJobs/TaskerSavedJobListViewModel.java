package com.wmdd.errandz.taskersJobs;

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

public class TaskerSavedJobListViewModel extends ViewModel {

    private Prefs sharedPreference;
    private ErrandzApi errandzApi;

    private MutableLiveData<ArrayList<Job>> savedJobListMutableLiveData;

    public void init() {

        sharedPreference = Prefs.getInstance();
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);

        savedJobListMutableLiveData = new MutableLiveData<>();

    }

    public void makeTaskerSavedJobApiCall() {

        int userId = sharedPreference.getUserID();
        String idToken = sharedPreference.getIDToken();
        String uid = sharedPreference.getUID();

        errandzApi.taskerSavedJobListRequest(idToken, uid, userId).enqueue(new Callback<JobListResponse>() {
            @Override
            public void onResponse(Call<JobListResponse> call, Response<JobListResponse> response) {
                if(response.isSuccessful()) {
                    savedJobListMutableLiveData.setValue(response.body().getJobArrayList());
                }
            }

            @Override
            public void onFailure(Call<JobListResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<ArrayList<Job>> getSavedJobListMutableLiveData() {
        return savedJobListMutableLiveData;
    }

}
