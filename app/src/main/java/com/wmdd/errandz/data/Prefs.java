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

    public void saveUID(String uid) {
        sharedPreferences.edit().putString("social_user_id", uid).commit();
    }

    public String getUID() {
        return sharedPreferences.getString("social_user_id", "");
    }

    public void saveIDToken(String idToken) {
        sharedPreferences.edit().putString("id_token", idToken).commit();
    }

    public String getIDToken() {
        return sharedPreferences.getString("id_token", "");
    }
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

    public void saveToken(String token) {
        sharedPreferences.edit().putString("token", token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void saveProfileImage(String profileImage) {
        sharedPreferences.edit().putString("profile_image", profileImage).apply();
    }

    public String getProfileImage() {
        return sharedPreferences.getString("profile_image", "");
    }

    public void saveUserBio(String bio) {
        sharedPreferences.edit().putString("bio", bio).apply();
    }

    public String getUserBio() {
        return sharedPreferences.getString("bio", "");
    }

    public void saveFullAddress(String fullAddress) {
        sharedPreferences.edit().putString("full_address", fullAddress).apply();
    }

    public String getFullAddress() {
        return sharedPreferences.getString("full_address", "");
    }

    public void clear() {
        saveUserID(0);
        saveUID("");
        saveFullAddress("");
        saveUserBio("");
        saveEmailID("");
        saveProfileImage("");
        saveUserType(0);
    }



}
