package com.wmdd.errandz.notifications;

import android.app.Service;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.LoginResponse;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;

public class ErrandzFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("TOKEN", token);
        Prefs.getInstance().saveToken(token);
        if(Prefs.getInstance().getUserID() > 0) {
            callPushTokenApi(token);
        }

//        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("Message", remoteMessage.toString());
    }

    private void callPushTokenApi(String token) {
        ErrandzApi errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        errandzApi.updatePushToken(Prefs.getInstance().getUserID(), token).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call,
                                   retrofit2.Response<CommonResponse> loginResponse) {
                if (loginResponse.isSuccessful()) {
                    Prefs.getInstance().saveToken(token);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
}
