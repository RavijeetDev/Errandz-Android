package com.wmdd.errandz.userProfileEdit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.address.AddressActivity;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.databinding.ActivityUserProfileEditBinding;
import com.wmdd.errandz.hirerHome.HirerHomeActivity;
import com.wmdd.errandz.main.ErrandzApplication;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeActivity;
import com.wmdd.errandz.userProfile.UserProfileViewModel;
import com.wmdd.errandz.util.Permissions;
import com.wmdd.errandz.util.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.wmdd.errandz.util.Constants.FROM_ACTIVITY;

public class UserProfileEditActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetOpenCameraAndGallery.CallBackListener {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;

    private final int STORAGE_PERMISSION_REQUEST_CODE_CAMERA = 0;
    private final int STORAGE_PERMISSION_REQUEST_CODE_GALLERY = 1;
    private final int IMAGE_FROM_CAMERA = 200;
    private final int IMAGE_SELECT_FILE = 300;

    private static final String FILE_PROVIDER_AUTHORITY =
            ErrandzApplication.getInstance().getPackageName()
                    + ".fileprovider";

    private UserProfileEditViewModel userProfileEditViewModel;
    private ActivityUserProfileEditBinding binding;

    private boolean calledFromLogin;
    private com.wmdd.errandz.bean.Address address;
    private String profileImageUrl;
    private User user;

    private Snackbar snackbar;
    private BottomSheetOpenCameraAndGallery bottomSheetOpenCameraAndGallery;
    private File cameraImageFile, galleryImageFile, photoImageUrl;
    private Uri profileImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile_edit);

        user = getIntent().getParcelableExtra("USER");
        calledFromLogin = getIntent().getStringExtra(FROM_ACTIVITY) != null;

        binding.emailTextInputEditText.setEnabled(false);
        binding.dobTextInputEditText.setEnabled(false);

        binding.updateButton.setOnClickListener(this);
        binding.addressTextInputLayout.setOnClickListener(this);
        binding.addressTextInputEditText.setOnClickListener(this);
        binding.changeProfileTextView.setOnClickListener(this);
        binding.userProfileImageView.setOnClickListener(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        PlacesClient placesClient = Places.createClient(this);

        initializeViewModel();
        setValuesInLayouts();

    }

    private void setValuesInLayouts() {

        if (user != null) {
            binding.firstNameTextInputEditText.setText(user.getFirstName());
            binding.lastNameTextInputEditText.setText(user.getLastName());
            binding.emailTextInputEditText.setText(user.getEmailID());
            binding.dobTextInputEditText.setText(user.getDateOfBirth());
            binding.bioTextInputEditText.setText(user.getBio());

            if (!user.getProfileImage().isEmpty()) {
                Picasso.get()
                        .load(user.getProfileImage())
                        .into(binding.userProfileImageView);
            }

            address = user.getAddress();
            binding.addressTextInputEditText.setText(user.getAddress().getFullAddress());
        } else {
            binding.progressBarView.setVisibility(View.VISIBLE);
            userProfileEditViewModel.getUserInfo();
        }
    }

    private void initializeViewModel() {

        userProfileEditViewModel = ViewModelProviders.of(this).get(UserProfileEditViewModel.class);
        userProfileEditViewModel.init();

        userProfileEditViewModel.getResponse().observe(this, response -> {

            binding.progressBarView.setVisibility(View.GONE);

            if (response.getStatus().equals("success")) {
                if (calledFromLogin) {
                    if (Prefs.getInstance().getUserType() == 1) {
                        Intent intent = new Intent(this, HirerHomeActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, TaskerHomeActivity.class);
                        startActivity(intent);
                    }
                } else {
                    finish();
                }
            }
        });

        userProfileEditViewModel.getUserMutableLiveData().observe(this, userInfo -> {
            binding.progressBarView.setVisibility(View.GONE);
            if (userInfo != null) {
                user = userInfo;
                setValuesInLayouts();
            }
        });
    }

    public void onSearchCalled() {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("CA")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_button:
                updateUserInfo();
                break;
            case R.id.address_text_input_layout:
            case R.id.address_text_input_edit_text:
                onSearchCalled();
                break;
            case R.id.user_profile_image_view:
            case R.id.change_profile_text_view:
                openCameraAndGalleryBottomSheet();
                break;
        }
    }

    private void openCameraAndGalleryBottomSheet() {
        bottomSheetOpenCameraAndGallery = new BottomSheetOpenCameraAndGallery(this);
        bottomSheetOpenCameraAndGallery.show(getSupportFragmentManager(),
                bottomSheetOpenCameraAndGallery.getTag());
    }

    private void updateUserInfo() {

        String firstName = binding.firstNameTextInputEditText.getText().toString();
        String lastName = binding.lastNameTextInputEditText.getText().toString();
        String bio = binding.bioTextInputEditText.getText().toString();
        profileImageUrl = user.getProfileImage();
        String addressJson = new Gson().toJson(address);

        if (firstName.isEmpty()) {
            binding.firstNameTextInputLayout.setError(getString(R.string.empty_first_name));
        } else if (!Utility.isNameValid(firstName)) {
            binding.firstNameTextInputLayout.setError(getString(R.string.first_name_invalid));
        } else if (lastName.isEmpty()) {
            binding.lastNameTextInputLayout.setError(getString(R.string.empty_last_name));
        } else if (!Utility.isNameValid(lastName)) {
            binding.lastNameTextInputLayout.setError(getString(R.string.last_name_invalid));
        } else if (bio.isEmpty()) {
            binding.bioTextInputLayout.setError("Please enter your bio");
        } else if (profileImageUrl == null || profileImageUrl.isEmpty()) {
            showSnackbar("Please upload your profile Image");
        } else {
            binding.progressBarView.setVisibility(View.VISIBLE);
            userProfileEditViewModel.updateUserProfileApiCall(firstName, lastName, bio, addressJson);
        }

    }

    private void showSnackbar(String message) {

        if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
        snackbar = Snackbar.make(binding.firstNameTextInputEditText, message,
                Snackbar.LENGTH_SHORT);
        Utility.initializeSnackBar(snackbar, this);
        snackbar.show();
    }

    private void uploadImageOnFirebase(String imageUrl) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(imageUrl));
        StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("Exception", exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getFirebaseImageUr(file);
                // ...
            }
        });


    }

    private void getFirebaseImageUr(Uri fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child("images/" + fileName.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(fileName);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    binding.progressBarView.setVisibility(View.GONE);
                    profileImageUri = task.getResult();
                    Picasso.get()
                            .load(profileImageUrl)
                            .into(binding.userProfileImageView);
                } else {

                }
            }
        });
    }

    @Override
    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            if (Permissions.getInstance().checkForPermissionInActivity(this,
                    STORAGE_PERMISSION_REQUEST_CODE_GALLERY,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                startActivityForResult(galleryIntent, IMAGE_SELECT_FILE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void openCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (Permissions.getInstance().checkForPermissionInActivity(this,
                    STORAGE_PERMISSION_REQUEST_CODE_CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                cameraImageFile = Utility.createTempImageFile();

                Uri cameraImageURI = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY,
                        cameraImageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageURI);

                if (cameraImageFile != null) {
                    photoImageUrl = cameraImageFile.getAbsoluteFile();
                    startActivityForResult(cameraIntent, IMAGE_FROM_CAMERA);
                }


            }

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);
                String fullAddress = place.getAddress();
                binding.addressTextInputEditText.setText(fullAddress);

                try {
                    List<Address> addresses;
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                        String house = addresses.get(0).getSubThoroughfare();
                        String street = addresses.get(0).getThoroughfare();
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        Log.e("House No: ", "" + house);
                        Log.e("Street: ", "" + street);
                        Log.e("AddressCity: ", "" + city);
                        Log.e("AddressState: ", "" + state);
                        Log.e("AddressCountry: ", "" + country);
                        Log.e("AddressPostal: ", "" + postalCode);
                        Log.e("AddressLatitude: ", "" + place.getLatLng().latitude);
                        Log.e("AddressLongitude: ", "" + place.getLatLng().longitude);

                        address = new com.wmdd.errandz.bean.Address();
                        address.setAddressID(user.getAddress().getAddressID());
                        address.setFullAddress(fullAddress);
                        address.setStreetAddress("" + house + " " + street);
                        address.setCity(city);
                        address.setProvince(state);
                        address.setPostalCode(postalCode);
                        address.setCountry(country);
                        address.setLatitude(String.valueOf(place.getLatLng().latitude));
                        address.setLongitude(String.valueOf(place.getLatLng().longitude));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //setMarker(latLng);
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(UserProfileEditActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                Log.i("UserProfileEditActivity", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_FROM_CAMERA) {

            binding.progressBarView.setVisibility(View.VISIBLE);
            Log.e("TAG", photoImageUrl.toString());
            uploadImageOnFirebase(photoImageUrl.toString());
            bottomSheetOpenCameraAndGallery.dismiss();

        } else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_SELECT_FILE) {
            if (data != null) {
                if (data.getData() != null) {
                    binding.progressBarView.setVisibility(View.VISIBLE);
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                        String imgPath = getPathFromURI(selectedImage);
                        photoImageUrl = new File(imgPath.toString());
                        Log.e("TAG", photoImageUrl.toString());
                        uploadImageOnFirebase(photoImageUrl.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            bottomSheetOpenCameraAndGallery.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case STORAGE_PERMISSION_REQUEST_CODE_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, getString(R.string.cant_load_image),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case STORAGE_PERMISSION_REQUEST_CODE_GALLERY:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, getString(R.string.cant_load_image),
                            Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}