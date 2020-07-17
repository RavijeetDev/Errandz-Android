package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

public class TaskerHomeDataResponse {

    @SerializedName("result")
    private Response response;

    @SerializedName("data")
    private TaskerHomeData taskerHomeData;

    public Response getResponse() {
        return response;
    }

    public TaskerHomeData getTaskerHomeData() {
        return taskerHomeData;
    }
}
