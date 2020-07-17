package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

public class JobInfoResponse {

    @SerializedName("result")
    private Response response;

    @SerializedName("data")
    private JobDescription jobDescription;

    public Response getResponse() {
        return response;
    }

    public JobDescription getJobDescription() {
        return jobDescription;
    }
}
