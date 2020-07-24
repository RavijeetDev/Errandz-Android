package com.wmdd.errandz.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;
import com.wmdd.errandz.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Job implements Parcelable {

    @SerializedName("jobID")
    private int jobID;

    private String jobName;

    private String jobWage;

    @SerializedName("date")
    private long dateTimestamp;

    private String description;

    private int taskerID;

    private int jobCategory;

    private int status;

    private int hirerID;

    private int jobStatusID;

    private Address address;


    protected Job(Parcel in) {
        jobID = in.readInt();
        jobName = in.readString();
        jobWage = in.readString();
        dateTimestamp = in.readLong();
        description = in.readString();
        taskerID = in.readInt();
        jobCategory = in.readInt();
        status = in.readInt();
        hirerID = in.readInt();
        jobStatusID = in.readInt();
        address = in.readParcelable(Address.class.getClassLoader());
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public int getJobID() {
        return jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobWage() {
        return jobWage;
    }

    public long getDateTimestamp() {
        return dateTimestamp;
    }

    public String getDescription() {
        return description;
    }

    public int getTaskerID() {
        return taskerID;
    }

    public int getStatus() {
        return status;
    }

    public int getHirerID() {
        return hirerID;
    }

    public int getJobStatusID() {
        return jobStatusID;
    }

    public String getJobDate() {
        Date date = new Date();
        date.setTime(dateTimestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy ", Locale.CANADA);
        return sdf.format(date);
    }

    public String getJobCategoryName() {
        return Constants.jobCategory[jobCategory - 1];
    }

    public int getJobCategoryImage() {
        return Constants.jobCategoryIcon[jobCategory - 1];
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(jobID);
        dest.writeString(jobName);
        dest.writeString(jobWage);
        dest.writeLong(dateTimestamp);
        dest.writeString(description);
        dest.writeInt(taskerID);
        dest.writeInt(jobCategory);
        dest.writeInt(status);
        dest.writeInt(hirerID);
        dest.writeInt(jobStatusID);
        dest.writeParcelable(address, flags);
    }
}
