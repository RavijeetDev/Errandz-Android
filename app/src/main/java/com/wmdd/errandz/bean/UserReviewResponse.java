package com.wmdd.errandz.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserReviewResponse {

    @SerializedName("result")
    private CommonResponse commonResponse;

    @SerializedName("data")
    private ArrayList<Review> reviewArrayList;

    public CommonResponse getCommonResponse() {
        return commonResponse;
    }

    public ArrayList<Review> getReviewArrayList() {
        return reviewArrayList;
    }
}
