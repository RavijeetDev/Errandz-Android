package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TaskerHomeData {

    @SerializedName("approved_jobs")
    private ArrayList<Job> approvedJobArrayList;

    @SerializedName("upcoming_jobs")
    private ArrayList<Job> nearbyJobArrayList;

    public ArrayList<Job> getApprovedJobArrayList() {
        return approvedJobArrayList;
    }

    public ArrayList<Job> getNearbyJobArrayList() {
        return nearbyJobArrayList;
    }
}
