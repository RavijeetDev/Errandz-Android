package com.wmdd.errandz.util;

import com.wmdd.errandz.R;

public interface Constants {

  //  String BASE_URL = "https://192.168.1.80:3000/";  // Local Machine

    // IP for Server Hosted on AWS EC2 Instance
   String BASE_URL = "http://54.167.159.136:3000/";

    int HIRER = 1;
    int TASKER = 2;

    String[] jobCategory = {
            "Carpentry", "Cleaning", "Home Repairs", "Mounting", "Moving", "Organizing",
            "Packing and Unpacking", "Paint", "Pet Sitting", "Yard Work"
    };

    int[] jobCategoryIcon = {
            R.drawable.carpentry,
            R.drawable.cleaning,
            R.drawable.home_repairs,
            R.drawable.mounting,
            R.drawable.moving,
            R.drawable.organizing,
            R.drawable.packing_unpacking,
            R.drawable.paint,
            R.drawable.petsitting,
            R.drawable.yardwork
    };

    int[] jobCategoryImage = {
            R.drawable.carpentry_background,
            R.drawable.cleaning_background,
            R.drawable.home_repairs_background,
            R.drawable.home_repairs_background,
            R.drawable.moving_background,
            R.drawable.organizing_background,
            R.drawable.packing_background,
            R.drawable.paint_background,
            R.drawable.petsitting_background,
            R.drawable.gardening_background
    };

    String FROM_ACTIVITY = "from_activity";
}
