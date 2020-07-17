package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

public class HirerHomeData {

    @SerializedName("number_of_jobs")
    private int numberOfJobs;

    public int getNumberOfJobs() {
        return numberOfJobs;
    }
}
