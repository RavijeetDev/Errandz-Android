package com.wmdd.errandz.login.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.LoginResponse;
import com.wmdd.errandz.bean.Response;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpViewModel extends ViewModel {

    private ErrandzApi errandzApi;
    private FirebaseAuth firebaseAuth;

    private MutableLiveData<Response> responseMutableLiveData;
    private MutableLiveData<String> errorResponse;

    private String firstName;
    private String lastName;
    private String emailID;
    private long dateOfBirthTimestamp;
    private int userType;
    private int loginType;

    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        responseMutableLiveData = new MutableLiveData<>();
        errorResponse = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signUpRequest(String firstName, String lastName, String emailID, String password,
                              long dateOFBirthTimestamp, int userType, int loginType, AppCompatActivity activity) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
        this.dateOfBirthTimestamp = dateOFBirthTimestamp;
        this.userType = userType;
        this.loginType = loginType;

        signUpInFirebase(emailID, password, activity);
    }

    private void signUpInFirebase(String emailID, String password, AppCompatActivity activity) {
        firebaseAuth.createUserWithEmailAndPassword(emailID, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.e("user - firebase - uid", user.getUid());
                            signUpApiCall(user.getUid());

                        } else {
                            errorResponse.setValue(task.getException().getMessage());
                        }
                    }
                });
    }

    private void signUpApiCall(String uid) {

        errandzApi.signUpApiRequest(firstName, lastName, dateOfBirthTimestamp, emailID, userType, loginType, uid, "")
                .enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call,
                                   retrofit2.Response<CommonResponse> response) {
                if (response.isSuccessful()) {

                    responseMutableLiveData.setValue(response.body().getResponse());

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    public MutableLiveData<Response> getSignUpResponse() {
        return responseMutableLiveData;
    }

    public MutableLiveData<String> getErrorResponse() {
        return errorResponse;
    }


    @Override
    protected void onCleared() {
        super.onCleared();

    }


}
