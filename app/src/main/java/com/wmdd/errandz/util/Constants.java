package com.wmdd.errandz.util;

import com.wmdd.errandz.R;

public interface Constants {

    String LOCAL_BASE_URL = "http://192.168.1.80:3000/";  // Local Machine

    // IP for Server Hosted on AWS EC2 Instance
//    String LOCAL_BASE_URL = "http://54.167.159.136:3000/";

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

    String FROM_ACTIVITY = "from_activity";
}
