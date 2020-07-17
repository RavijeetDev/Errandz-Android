package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("result")
    private Response response;

    @SerializedName("data")
    private User user;


    public Response getResponse() {
        return response;
    }

    public User getUser() {
        return user;
    }
}
