package com.wmdd.errandz.hirerHome;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.HirerHomeData;
import com.wmdd.errandz.bean.HirerHomeResponse;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HirerHomeViewModel extends ViewModel {

    private ErrandzApi errandzApi;
    private MutableLiveData<HirerHomeData> hirerHomeDataMutableLiveData;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        hirerHomeDataMutableLiveData = new MutableLiveData<>();
    }


    public void makeHirerHomeDataApiCall() {

        int userID = Prefs.getInstance().getUserID();

        errandzApi.hirerHomeDataResponse(userID).enqueue(new Callback<HirerHomeResponse>() {
            @Override
            public void onResponse(Call<HirerHomeResponse> call, Response<HirerHomeResponse> response) {
                if(response.isSuccessful()) {
                    hirerHomeDataMutableLiveData.setValue(response.body().getHirerHomeData());
                }
            }

            @Override
            public void onFailure(Call<HirerHomeResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<HirerHomeData> getHomeData() {
        return hirerHomeDataMutableLiveData;
    }
}
