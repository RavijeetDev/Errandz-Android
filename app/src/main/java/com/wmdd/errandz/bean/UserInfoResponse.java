package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

public class UserInfoResponse {

    @SerializedName("result")
    public Response response;

    @SerializedName("data")
    public User user;

    public Response getResponse() {
        return response;
    }

    public User getUser() {
        return user;
    }
}
