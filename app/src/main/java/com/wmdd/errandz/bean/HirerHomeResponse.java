package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

public class HirerHomeResponse {

    @SerializedName("result")
    private Response response;

    @SerializedName("data")
    private HirerHomeData hirerHomeData;

    public Response getResponse() {
        return response;
    }

    public HirerHomeData getHirerHomeData() {
        return hirerHomeData;
    }
}
