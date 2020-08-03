package com.wmdd.errandz.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by Randeep Kaur on 24/08/2018 1:13 PM
 * Copyright (c) 2018-2019 . All rights reserved.
 * Pulp Strategy Communications Pvt Ltd.
 * Last Modified Randeep Kaur on 24/08/2018 1:13 PM
 */

public class Permissions {

    private final String TAG = Permissions.class.getSimpleName();

    public static Permissions permissions;

    public static Permissions getInstance(){
        if(permissions == null){
            permissions = new Permissions();
        }

        return permissions;
    }

    public boolean checkForPermissionInActivity(AppCompatActivity activity, int permissionType,
                                                String permission){
        if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionType);
            return false;
        } else {
            return true;
        }
    }

    public boolean checkForPermissionInFragment(Fragment fragment, int permissionType, String permission){

        if(ActivityCompat.checkSelfPermission(fragment.getContext(),
                permission) != PackageManager.PERMISSION_GRANTED){
            fragment.requestPermissions(new String[]{permission},
                    permissionType);
            return false;
        } else {
            return true;
        }
    }

}
