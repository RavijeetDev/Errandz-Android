package com.wmdd.errandz.util;

import com.wmdd.errandz.R;

public interface Constants {

    String LOCAL_BASE_URL = "http://192.168.1.65:3000/";

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
}
