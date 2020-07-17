package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

public class JobDescription {

    @SerializedName("jobInfo")
    private Job job;

    @SerializedName("userInfo")
    private User user;

    public Job getJob() {
        return job;
    }

    public User getUser() {
        return user;
    }
}
