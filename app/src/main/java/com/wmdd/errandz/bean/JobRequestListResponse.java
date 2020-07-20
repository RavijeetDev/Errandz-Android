package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JobRequestListResponse {

    @SerializedName("result")
    private Response response;

    @SerializedName("data")
    private ArrayList<JobDescription> jobDescriptionArrayList;

    public Response getResponse() {
        return response;
    }

    public ArrayList<JobDescription> getJobDescriptionArrayList() {
        return jobDescriptionArrayList;
    }
}
