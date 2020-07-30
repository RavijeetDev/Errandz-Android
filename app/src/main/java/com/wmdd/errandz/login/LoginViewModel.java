package com.wmdd.errandz.login;

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
import com.google.firebase.auth.GetTokenResult;
import com.wmdd.errandz.api.Api;
import com.wmdd.errandz.api.ErrandzApi;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.Response;
import com.wmdd.errandz.bean.LoginResponse;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginViewModel extends ViewModel {


    private static final String TAG = LoginViewModel.class.getSimpleName();

    private ErrandzApi errandzApi;
    private Prefs sharedPreferences;
    private FirebaseAuth firebaseAuth;

    private MutableLiveData<Response> responseMutableLiveData;
    private MutableLiveData<String> errorResponse;

    private String uId;
    private String idToken;
    private String emailID;
    private int loginType;




    public void init() {
        errandzApi = Api.getRetrofitClient().create(ErrandzApi.class);
        responseMutableLiveData = new MutableLiveData<>();
        errorResponse = new MutableLiveData<>();
        sharedPreferences = Prefs.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getUid() != null && firebaseAuth.getUid().isEmpty()) {
            firebaseAuth.signOut();
        }
    }


    public void makeFirebaseLoginCall(String emailID, String password, int loginType, AppCompatActivity activity) {

        this.emailID = emailID;
        this.loginType = loginType;

        firebaseAuth.signInWithEmailAndPassword(emailID, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Firebase - Sign In Successful");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            uId = user.getUid();
                            getIDToken();
                        } else {
                            // If sign in fails, display a message to the user.
                            errorResponse.setValue(task.getException().getMessage());
                        }

                    }
                });


    }

    private void getIDToken() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            makeLoginCall();
                        } else {
                            errorResponse.setValue(task.getException().getMessage());
                        }
                    }
                });
    }

    private void makeLoginCall() {

        errandzApi.loginRequest(emailID, uId, loginType, sharedPreferences.getToken()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   retrofit2.Response<LoginResponse> loginResponse) {
                if (loginResponse.isSuccessful() && loginResponse.body().getResponse().getStatus().equals("success")) {

                    User user = loginResponse.body().getUser();
                    sharedPreferences.saveUserID(user.getUserID());
                    sharedPreferences.saveEmailID(user.getEmailID());
                    sharedPreferences.saveUserType(user.getUserType());
                    sharedPreferences.saveProfileImage(user.getProfileImage());
                    sharedPreferences.saveUserBio(user.getBio());
                    sharedPreferences.saveUID(uId);
                    sharedPreferences.saveIDToken(idToken);

                    if (user.getAddress() != null && user.getAddress().getFullAddress() != null) {
                        sharedPreferences.saveFullAddress(user.getAddress().getFullAddress());
                    }

                    responseMutableLiveData.setValue(loginResponse.body().getResponse());
                } else {

                    errorResponse.setValue("Google not registered");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                errorResponse.setValue("task.getException().getMessage()");
            }
        });
    }

    public void loginWithGoogle(String uid, String emailID) {

        this.emailID = emailID;
        this.uId = uid;
        this.loginType = 2;

        makeLoginCall();

    }

    public void makeGoogleLogin(String firstName, String lastName, String emailID,
                                long dateOfBirthTimestamp, int userType, String profileImage, String uid) {

        this.uId = uid;
        this.emailID = emailID;
        this.loginType = 2;

        errandzApi.signUpApiRequest(firstName, lastName, dateOfBirthTimestamp, emailID, userType, loginType, uid, profileImage)
                .enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call,
                                           retrofit2.Response<CommonResponse> response) {
                        if (response.isSuccessful() && response.body().getResponse().getStatus().equals("success")) {

                            makeLoginCall();

                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }

    public MutableLiveData<Response> getLoginResponse() {
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
