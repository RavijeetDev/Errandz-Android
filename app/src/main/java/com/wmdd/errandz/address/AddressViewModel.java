package com.wmdd.errandz.address;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.Address;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.Response;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.bean.UserInfoResponse;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;

public class AddressViewModel extends ViewModel {

    private static final int UPDATE_ADDRESS_API = 1;
    private static final int GET_USER_INFO_API = 2;

    private Prefs sharedPreferences;
    private ErrandzApi errandzApi;

    private MutableLiveData<Response> addAddressLiveData;
    private MutableLiveData<User> userMutableLiveData;

    private String uid;
    private String idToken;
    private int userID;

    private Address address;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        addAddressLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
        sharedPreferences = Prefs.getInstance();

        uid = sharedPreferences.getUID();
        idToken = sharedPreferences.getIDToken();
        userID = sharedPreferences.getUserID();
    }

    public void callUserAddressApi(Address address) {

        this.address = address;

        errandzApi.updateAddressRequest(idToken, uid, userID, address.getStreetAddress(), address.getCity(),
                address.getProvince(), address.getPostalCode(), address.getCountry(), address.getFullAddress(),
                address.getLatitude(), address.getLongitude())
                .enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                        if (response.isSuccessful()) {

                            String status = response.body().getResponse().getStatus();
                            String message = response.body().getResponse().getMessage();

                            if(status.equals("success")) {
                                addAddressLiveData.setValue(response.body().getResponse());
                            } else if(status.equals("404") && message.equals("Token expired")) {
                                getNewToken(UPDATE_ADDRESS_API);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
    }

    private void getNewToken(int apiCall) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            idToken = task.getResult().getToken();
                            sharedPreferences.saveIDToken(idToken);

                            if(apiCall == UPDATE_ADDRESS_API) {
                                callUserAddressApi(address);
                            } else {
                                getUserInfo();
                            }
                        } else {
                            Log.e("Token", "token issue");
                        }
                    }
                });
    }

    public void getUserInfo() {

        errandzApi.userInfoRequest(idToken, uid, userID)
                .enqueue(new Callback<UserInfoResponse>() {
                    @Override
                    public void onResponse(Call<UserInfoResponse> call, retrofit2.Response<UserInfoResponse> response) {
                        if (response.isSuccessful()) {

                            String status = response.body().getResponse().getStatus();
                            String message = response.body().getResponse().getMessage();

                            if(status.equals("success")) {
                                userMutableLiveData.setValue(response.body().getUser());
                            } else if(status.equals("404") && message.equals("Token expired")) {
                                getNewToken(GET_USER_INFO_API);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfoResponse> call, Throwable t) {

                    }
                });
    }

    public MutableLiveData<Response> getResponse() {
        return addAddressLiveData;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
