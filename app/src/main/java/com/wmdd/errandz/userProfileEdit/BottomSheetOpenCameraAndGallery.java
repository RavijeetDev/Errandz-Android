package com.wmdd.errandz.userProfileEdit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.wmdd.errandz.R;
import com.wmdd.errandz.databinding.OpenSheetCameraGalleryBinding;
import com.wmdd.errandz.main.ErrandzApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

/**
 * Created by Ruchi Khare on 12/11/18 1:00 PM
 * Copyright (c) 2018-2019 . All rights reserved.
 * Pulp Strategy Communications Pvt Ltd.
 * Last modified Ruchi Khare on 12/11/18 1:00 PM
 */
@SuppressLint("ValidFragment")
public class BottomSheetOpenCameraAndGallery extends BottomSheetDialogFragment implements View.OnClickListener {

    private final String TAG = BottomSheetOpenCameraAndGallery.class.getSimpleName();
    private static final String FILE_PROVIDER_AUTHORITY =
            ErrandzApplication.getInstance().getPackageName()
                    + ".fileprovider";

    private final int STORAGE_PERMISSION_REQUEST_CODE_CAMERA = 0;
    private final int STORAGE_PERMISSION_REQUEST_CODE_GALLERY = 1;
    private final int IMAGE_FROM_CAMERA = 100;
    private final int IMAGE_SELECT_FILE = 200;

    private View rootView;
    private TextView takePictureTextView;
    private TextView galleryTextView;
    private File cameraImageFile, galleryImageFile, photoImageUrl;
    private CallBackListener callBackListener;

    private boolean isShowing = true;

    private OpenSheetCameraGalleryBinding binding;

    public interface CallBackListener {
        void openGallery();
        void openCamera();
    }



    @SuppressLint("ValidFragment")
    public BottomSheetOpenCameraAndGallery(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.open_sheet_camera_gallery,
                container, false);

        binding.takePicture.setOnClickListener(this);
        binding.photoGallery.setOnClickListener(this);

        return binding.getRoot();

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_gallery:
                callBackListener.openGallery();
                break;
            case R.id.take_picture:
                callBackListener.openCamera();
                break;
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet =  bottomSheetDialog
                        .findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        isShowing = false;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShowing = false;
    }

    public boolean getBottomSheetIsVisible() {
        return isShowing;
    }

}
