package com.wmdd.errandz.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Review {

    private int ID;

    private int reviewerID;

    private String reviewerProfilePic;

    private String reviewerName;

    private int jobID;

    private String jobName;

    private float rating;

    private long reviewDate;

    private String review;

    public int getReviewerID() {
        return reviewerID;
    }

    public int getID() {
        return ID;
    }

    public String getReviewerProfilePic() {
        return reviewerProfilePic;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public int getJobID() {
        return jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public float getRating() {
        return rating;
    }

    public String getReviewDate() {

        Date date = new Date();
        date.setTime(reviewDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy ", Locale.CANADA);
        return sdf.format(date);

    }

    public String getReview() {
        return review;
    }
}
