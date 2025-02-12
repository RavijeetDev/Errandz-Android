package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HirerHomeData {

    @SerializedName("number_of_jobs")
    private int numberOfJobs;

    @SerializedName("jobList")
    private ArrayList<JobDescription> jobDescriptionArrayList;


    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    public ArrayList<JobDescription> getJobDescriptionArrayList() {
        return jobDescriptionArrayList;
    }
}
