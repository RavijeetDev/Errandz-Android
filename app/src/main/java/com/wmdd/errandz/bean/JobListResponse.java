package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JobListResponse {

    @SerializedName("result")
    private Response response;

    @SerializedName("data")
    private ArrayList<Job> jobArrayList;

    public Response getResponse() {
        return response;
    }

    public ArrayList<Job> getJobArrayList() {
        return jobArrayList;
    }
}
