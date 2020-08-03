package com.wmdd.errandz.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.wmdd.errandz.R;
import com.wmdd.errandz.main.ErrandzApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static String getDateString(long dateTimestamp) {
        Date date = new Date();
        date.setTime(dateTimestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy ", Locale.CANADA);
        return sdf.format(date);
    }

    public static boolean isEmailValid(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public static boolean isNameValid(String name) {
        String regexName = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regexName);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static int getAge(long dateOfBirth) {

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

    public static void initializeSnackBar(Snackbar snackbar, Context context) {

        View snackbarView = snackbar.getView();
//        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.image_placeholder_color));
        TextView snackBarTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

        snackBarTextView.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    /**
     * ------------------------ temporary file path ------------------------
     */
    public static File createTempImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = ErrandzApplication.getInstance().getExternalCacheDir();

            return File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (Exception e) {
            return null;
        }
    }

}
