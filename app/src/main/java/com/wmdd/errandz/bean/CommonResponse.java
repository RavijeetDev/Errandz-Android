package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("result")
    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
