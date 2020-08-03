package com.wmdd.errandz.hirerHome;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.HirerHomeData;
import com.wmdd.errandz.bean.HirerHomeResponse;
import com.wmdd.errandz.bean.JobDescription;
import com.wmdd.errandz.data.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HirerHomeViewModel extends ViewModel {

    private Prefs sharedPreference;
    private ErrandzApi errandzApi;

    private MutableLiveData<ArrayList<JobDescription>> jobArrayList;
    private MutableLiveData<Integer> numberOfJobs;

    public void init() {
        sharedPreference = Prefs.getInstance();
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        jobArrayList = new MutableLiveData<>();
        numberOfJobs = new MutableLiveData<>();
    }


    public void makeHirerHomeDataApiCall() {

        int userID = sharedPreference.getUserID();
        String idToken = sharedPreference.getIDToken();
        String uid = sharedPreference.getUID();

        errandzApi.hirerHomeDataResponse(idToken, uid, userID).enqueue(new Callback<HirerHomeResponse>() {
            @Override
            public void onResponse(Call<HirerHomeResponse> call, Response<HirerHomeResponse> response) {
                if(response.isSuccessful()) {
                    HirerHomeData hirerHomeData = response.body().getHirerHomeData();
                    numberOfJobs.setValue(hirerHomeData.getNumberOfJobs());
                    jobArrayList.setValue(hirerHomeData.getJobDescriptionArrayList());
                }
            }

            @Override
            public void onFailure(Call<HirerHomeResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<ArrayList<JobDescription>> getJobArrayList() {
        return jobArrayList;
    }

    public MutableLiveData<Integer> getNumberOfJobs() {
        return numberOfJobs;
    }
}
