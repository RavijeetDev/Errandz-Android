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
import retrofit2.http.POST;

public interface ErrandzApi {

    @FormUrlEncoded
    @POST("signUp")
    Observable<CommonResponse> signUpApiRequest(
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("password") String password,
            @Field("dob") String dateOfBirth,
            @Field("emailID") String emailID,
            @Field("userType") int userType,
            @Field("loginType") int loginType,
            @Field("googleID") String googleID
    );

    @FormUrlEncoded
    @POST("emailVerification")
    Observable<CommonResponse> emailVerificationRequest(
            @Field("emailID") String emailID,
            @Field("activationCode") int activationCode
    );

    @FormUrlEncoded
    @POST("emailActivationCode")
    Observable<CommonResponse> emailActivationCodeRequest(
            @Field("emailID") String emailID
    );

    @FormUrlEncoded
    @POST("passwordActivationCode")
    Observable<CommonResponse> passwordActivationCodeRequest(
            @Field("emailID") String emailID
    );

    @FormUrlEncoded
    @POST("createNewPassword")
    Observable<CommonResponse> createNewPasswordRequest(
            @Field("emailID") String emailID,
            @Field("activationCode") int activationCode,
            @Field("password") String newPassword
    );

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginRequest(
            @Field("emailID") String emailID,
            @Field("loginType") int loginType,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("hirerHomeData")
    Call<HirerHomeResponse> hirerHomeDataResponse(
            @Field("hirerID") int userId
    );

    @FormUrlEncoded
    @POST("postJob")
    Call<CommonResponse> postJobRequest(
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
            @Field("hirerID") int userID
    );

    @FormUrlEncoded
    @POST("hirerJobHistoryList")
    Call<JobListResponse> hirerJobHistoryListRequest(
            @Field("hirerID") int userID
    );

    @FormUrlEncoded
    @POST("userInfo")
    Call<UserInfoResponse> userInfoRequest(
            @Field("userID") int userID
    );

    @FormUrlEncoded
    @POST("uploadUserInfo")
    Call<CommonResponse> uploadUserInfoRequest(
            @Field("userID") int userID,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("bio") String bio,
            @Field("address") String addressJson
            );

    @FormUrlEncoded
    @POST("taskerHomeData")
    Call<TaskerHomeDataResponse> taskerHomeDataRequest(
            @Field("taskerID") int userID
    );

    @FormUrlEncoded
    @POST("updateJobStatus")
    Call<CommonResponse> updateJobStatus(
            @Field("taskerID") int userID,
            @Field("hirerID") int hirerID,
            @Field("jobID") int jobID,
            @Field("status") int status,
            @Field("jobStatusID") int jobStatusID
    );

    @FormUrlEncoded
    @POST("deleteJobStatus")
    Call<CommonResponse> unsaveJob(
            @Field("jobStatusID") int jobStatusID);


    @FormUrlEncoded
    @POST("taskerAppliedJobList")
    Call<JobListResponse> taskerAppliedJobListRequest(
            @Field("taskerID") int taskerID);

    @FormUrlEncoded
    @POST("taskerSavedJobList")
    Call<JobListResponse> taskerSavedJobListRequest(
            @Field("taskerID") int taskerID);

    @FormUrlEncoded
    @POST("taskerJobInfo")
    Call<JobInfoResponse> taskerJobInfoRequest(
            @Field("jobID") int jobID,
            @Field("hirerID") int hirerId,
            @Field("taskerID") int userID);

    @FormUrlEncoded
    @POST("userReviewList")
    Call<UserReviewResponse> userReviewListRequest(
            @Field("userID") int userID);

    @FormUrlEncoded
    @POST("historyJobInfo")
    Call<JobInfoResponse> hirerJobInfoHistoryRequest(
            @Field("jobID") int jobID,
            @Field("hirerID") int userID,
            @Field("taskerID") int taskerID);

    @FormUrlEncoded
    @POST("reviewsOfJob")
    Call<UserReviewResponse> jobReviewListCall(
            @Field("jobID") int jobID,
            @Field("reviewerID") int userID);

    @FormUrlEncoded
    @POST("postReview")
    Call<CommonResponse> addNewReviewRequest(
            @Field("jobID") int jobID,
            @Field("userID") int userID,
            @Field("reviewerID") int taskerID,
            @Field("rating") float rating,
            @Field("review") String review);

    @FormUrlEncoded
    @POST("jobRequestList")
    Call<JobRequestListResponse> jobRequestListRequest(
            @Field("hirerID") int userID);
}
