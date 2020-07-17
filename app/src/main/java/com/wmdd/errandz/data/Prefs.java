package com.wmdd.errandz.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.wmdd.errandz.main.ErrandzApplication;

public class Prefs {

    private final String SHARED_PREFERENCES_NAME = "errandz_preferences";

    private static Prefs prefs;
    private SharedPreferences sharedPreferences;

    public static Prefs getInstance() {
        if (prefs == null) {
            prefs = new Prefs();
        }
        return prefs;
    }

    private Prefs() {
        sharedPreferences = ErrandzApplication.getInstance()
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserID(int userID) {
        sharedPreferences.edit().putInt("user_id", userID).commit();
    }

    public int getUserID() {
        return sharedPreferences.getInt("user_id", -1);
    }

//    public void saveUserFirstName(String firstName) {
//        sharedPreferences.edit().putString("first_name", firstName).apply();
//    }
//
//    public String getUserFirstName() {
//        return sharedPreferences.getString("first_name", "");
//    }
//
//    public void saveUserLastName(String lasttName) {
//        sharedPreferences.edit().putString("last_name", lasttName).apply();
//    }
//
//    public String getUserLastName() {
//        return sharedPreferences.getString("last_name", "");
//    }

    public void saveEmailID(String emailID) {
        sharedPreferences.edit().putString("emailID", emailID).apply();
    }

    public String getEmailID() {
        return sharedPreferences.getString("emailID", "");
    }

    public void saveUserType(int userType) {
        sharedPreferences.edit().putInt("user_type", userType).apply();
    }

    public int getUserType() {
        return sharedPreferences.getInt("user_type", -1);
    }

//    public void saveDateOfBirth(String dateOfBirth) {
//        sharedPreferences.edit().putString("dob", dateOfBirth).apply();
//    }
//
//    public String getDateOfBirth() {
//        return sharedPreferences.getString("dob", "");
//    }
//
//    public void saveProfileImage(String profileImage) {
//        sharedPreferences.edit().putString("profile_image", profileImage).apply();
//    }
//
//    public String getProfileImage() {
//        return sharedPreferences.getString("profile_image", "");
//    }
//
//    public void saveUserBio(String bio) {
//        sharedPreferences.edit().putString("bio", bio).apply();
//    }
//
//    public String getUserBio() {
//        return sharedPreferences.getString("bio", "");
//    }


}
