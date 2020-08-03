package com.wmdd.errandz.api;


import com.wmdd.errandz.bean.Address;
import com.wmdd.errandz.bean.HirerHomeResponse;
import com.wmdd.errandz.bean.JobInfoResponse;
import com.wmdd.errandz.bean.JobListResponse;
import com.wmdd.errandz.bean.HirerUpcomingJobListResponse;
import com.wmdd.errandz.bean.CommonResponse;
import com.wmdd.errandz.bean.JobRequestListResponse;
import com.wmdd.errandz.bean.LoginResponse;
import com.wmdd.errandz.bean.UserInfoResponse;
import com.wmdd.errandz.bean.TaskerHomeDataResponse;
import com.wmdd.errandz.bean.UserReviewResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ErrandzApi {

    @FormUrlEncoded
    @POST("signUp")
    Call<CommonResponse> signUpApiRequest(
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("dob") long dateOfBirth,
            @Field("emailID") String emailID,
            @Field("userType") int userType,
            @Field("loginType") int loginType,
            @Field("uid") String uid,
            @Field("profileImage") String profileImage
    );

//    @FormUrlEncoded
//    @POST("emailVerification")
//    Observable<CommonResponse> emailVerificationRequest(
//            @Field("emailID") String emailID,
//            @Field("activationCode") int activationCode
//    );
//
//    @FormUrlEncoded
//    @POST("emailActivationCode")
//    Observable<CommonResponse> emailActivationCodeRequest(
//            @Field("emailID") String emailID
//    );
//
//    @FormUrlEncoded
//    @POST("passwordActivationCode")
//    Observable<CommonResponse> passwordActivationCodeRequest(
//            @Field("emailID") String emailID
//    );
//
//    @FormUrlEncoded
//    @POST("createNewPassword")
//    Observable<CommonResponse> createNewPasswordRequest(
//            @Field("emailID") String emailID,
//            @Field("activationCode") int activationCode,
//            @Field("password") String newPassword
//    );

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginRequest(
            @Header("Authorization") String idToken,
            @Field("emailID") String emailID,
            @Field("uid") String uID,
            @Field("loginType") int loginType,
            @Field("token") String pushToken
    );

    @FormUrlEncoded
    @POST("updateNotificationToken")
    Call<CommonResponse> updatePushToken(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("userID") int userID,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("addAddress")
    Call<CommonResponse> updateAddressRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uid,
            @Field("userID") int userID,
            @Field("streetAddress") String streetAddress,
            @Field("city") String city,
            @Field("province")String province,
            @Field("postalCode") String postalCode,
            @Field("country") String country,
            @Field("fullAddress") String fullAddress,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("hirerHomeData")
    Call<HirerHomeResponse> hirerHomeDataResponse(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("hirerID") int userId
    );

    @FormUrlEncoded
    @POST("postJob")
    Call<CommonResponse> postJobRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("hirerID") int hirerID,
            @Field("jobName") String jobName,
            @Field("jobWage") String jobWage,
            @Field("date") long timestamp,
            @Field("description") String description,
            @Field("jobCategory") int jobCategory
    );

    @FormUrlEncoded
    @POST("hirerUpcomingJobList")
    Call<HirerUpcomingJobListResponse> hirerUpcomingJobListRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("hirerID") int userID
    );

    @FormUrlEncoded
    @POST("hirerJobHistoryList")
    Call<JobListResponse> hirerJobHistoryListRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("hirerID") int userID
    );

    @FormUrlEncoded
    @POST("userInfo")
    Call<UserInfoResponse> userInfoRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("userID") int userID
    );

    @FormUrlEncoded
    @POST("uploadUserInfo")
    Call<CommonResponse> uploadUserInfoRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("userID") int userID,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("bio") String bio,
            @Field("address") String addressJson);

    @FormUrlEncoded
    @POST("taskerHomeData")
    Call<TaskerHomeDataResponse> taskerHomeDataRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("taskerID") int userID
    );

    @FormUrlEncoded
    @POST("updateJobStatus")
    Call<CommonResponse> updateJobStatus(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("taskerID") int userID,
            @Field("hirerID") int hirerID,
            @Field("jobID") int jobID,
            @Field("status") int status,
            @Field("jobStatusID") int jobStatusID
    );

    @FormUrlEncoded
    @POST("deleteJobStatus")
    Call<CommonResponse> unsaveJob(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("jobStatusID") int jobStatusID);


    @FormUrlEncoded
    @POST("taskerAppliedJobList")
    Call<JobListResponse> taskerAppliedJobListRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("taskerID") int taskerID);

    @FormUrlEncoded
    @POST("taskerSavedJobList")
    Call<JobListResponse> taskerSavedJobListRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("taskerID") int taskerID);

    @FormUrlEncoded
    @POST("taskerJobInfo")
    Call<JobInfoResponse> taskerJobInfoRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("jobID") int jobID,
            @Field("hirerID") int hirerId,
            @Field("taskerID") int userID);

    @FormUrlEncoded
    @POST("userReviewList")
    Call<UserReviewResponse> userReviewListRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("userID") int userID);

    @FormUrlEncoded
    @POST("historyJobInfo")
    Call<JobInfoResponse> hirerJobInfoHistoryRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("jobID") int jobID,
            @Field("hirerID") int userID,
            @Field("taskerID") int taskerID);

    @FormUrlEncoded
    @POST("reviewsOfJob")
    Call<UserReviewResponse> jobReviewListCall(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("jobID") int jobID,
            @Field("reviewerID") int userID);

    @FormUrlEncoded
    @POST("postReview")
    Call<CommonResponse> addNewReviewRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("jobID") int jobID,
            @Field("userID") int userID,
            @Field("reviewerID") int taskerID,
            @Field("rating") float rating,
            @Field("review") String review);

    @FormUrlEncoded
    @POST("jobRequestList")
    Call<JobRequestListResponse> jobRequestListRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("hirerID") int userID);

    @FormUrlEncoded
    @POST("hirerUpcomingJobDescription")
    Call<JobInfoResponse> hirerJobInfoRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("jobID") int jobID);

    @FormUrlEncoded
    @POST("taskerHistoryJobInfo")
    Call<JobInfoResponse> taskerJobInfoHistoryRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("jobID") int jobID,
            @Field("taskerID") int userID,
            @Field("hirerID") int hirerID);

    @FormUrlEncoded
    @POST("taskerJobHistoryList")
    Call<JobListResponse> taskerJobHistoryListRequest(
            @Header("Authorization") String idToken,
            @Field("uid") String uID,
            @Field("taskerID") int userID);
}
