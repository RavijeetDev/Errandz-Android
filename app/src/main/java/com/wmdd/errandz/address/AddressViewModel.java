package com.wmdd.errandz.address;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.Address;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.Response;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;

public class AddressViewModel extends ViewModel {

    private ErrandzApi errandzApi;
    private MutableLiveData<Response> addAddressLiveData;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        addAddressLiveData = new MutableLiveData<>();
    }

    public void callUserAddressApi(Address address) {

        int userID = Prefs.getInstance().getUserID();

        errandzApi.updateAddressRequest(userID, address.getStreetAddress(), address.getCity(),
                address.getProvince(), address.getPostalCode(), address.getCountry(), address.getFullAddress(),
                address.getLatitude(), address.getLongitude())
                .enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            addAddressLiveData.setValue(response.body().getResponse());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
    }

    public MutableLiveData<Response> getResponse() {
        return addAddressLiveData;
    }
}
