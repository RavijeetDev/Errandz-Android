package com.wmdd.errandz.bean;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.wmdd.errandz.BuildConfig;

import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class User implements Parcelable {

    private int userID;

    private String firstName;

    private String lastName;

    private String emailID;

    private int userType;

    @SerializedName("dob")
    private long dateOfBirth;

    private String profileImage;

    private String bio;

    private int numberOfReviews;

    private double totalRating;

    private Address address;

    protected User(Parcel in) {
        userID = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        emailID = in.readString();
        userType = in.readInt();
        dateOfBirth = in.readLong();
        profileImage = in.readString();
        bio = in.readString();
        numberOfReviews = in.readInt();
        totalRating = in.readDouble();
        address = in.readParcelable(Address.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailID() {
        return emailID;
    }

    public int getUserType() {
        return userType;
    }

    public long getDateOfBirthTimestamp() {
        return dateOfBirth;
    }

    public String getProfileImage() {
        return profileImage == null ? "" : profileImage;
    }

    public String getBio() {
        return bio == null ? "" : bio;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public Address getAddress() {
        return address;
    }

    public String getDateOfBirth() {
        Date date = new Date();
        date.setTime(dateOfBirth);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy ", Locale.CANADA);
        return sdf.format(date);
    }

    public int getAge() {

        int age = 0;

        Calendar now = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(dateOfBirth);

        if (dob.after(now)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }
        int year1 = now.get(Calendar.YEAR);
        int year2 = dob.get(Calendar.YEAR);
        age = year1 - year2;

        int month1 = now.get(Calendar.MONTH);
        int month2 = dob.get(Calendar.MONTH);
        if (month2 > month1) {
            age--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = dob.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age--;
            }
        }

        return age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userID);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(emailID);
        dest.writeInt(userType);
        dest.writeLong(dateOfBirth);
        dest.writeString(profileImage);
        dest.writeString(bio);
        dest.writeInt(numberOfReviews);
        dest.writeDouble(totalRating);
        dest.writeParcelable(address, flags);
    }
}
