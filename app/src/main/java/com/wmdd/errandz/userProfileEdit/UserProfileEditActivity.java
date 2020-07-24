package com.wmdd.errandz.userProfileEdit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.userProfile.UserProfileViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class UserProfileEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;
    private User user;
    private UserProfileEditViewModel userProfileEditViewModel;
    private com.wmdd.errandz.bean.Address address;

    private ImageView profileImageView;
    private TextInputLayout firstNameTextInputLayout;
    private TextInputLayout lastNameTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout dobTextInputLayout;
    private TextInputLayout addressTextInputLayout;
    private TextInputLayout bioTextInputLayout;
    private Button updateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);

        user = getIntent().getParcelableExtra("USER");

        profileImageView = findViewById(R.id.user_profile_image_view);
        firstNameTextInputLayout = findViewById(R.id.first_name_text_input_layout);
        lastNameTextInputLayout = findViewById(R.id.last_name_text_input_layout);
        emailTextInputLayout = findViewById(R.id.email_text_input_layout);
        dobTextInputLayout = findViewById(R.id.dob_text_input_layout);
        addressTextInputLayout = findViewById(R.id.address_text_input_layout);
        bioTextInputLayout = findViewById(R.id.bio_text_input_layout);
        updateButton = findViewById(R.id.update_button);

        updateButton.setOnClickListener(this);

        emailTextInputLayout.getEditText().setEnabled(false);
        dobTextInputLayout.getEditText().setEnabled(false);

        addressTextInputLayout.setFocusable(false);
        addressTextInputLayout.setClickable(true);
        addressTextInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchCalled();
            }
        });
        ((TextInputEditText) findViewById(R.id.address_text_input_edit_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchCalled();
            }
        });

        String apiKey = getString(R.string.api_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        setValuesInLayouts();
        initializeViewModel();
    }

    private void setValuesInLayouts() {
        firstNameTextInputLayout.getEditText().setText(user.getFirstName());
        lastNameTextInputLayout.getEditText().setText(user.getLastName());
        emailTextInputLayout.getEditText().setText(user.getEmailID());
        dobTextInputLayout.getEditText().setText(user.getDateOfBirth());
        addressTextInputLayout.getEditText().setText(user.getAddress().getFullAddress());
        bioTextInputLayout.getEditText().setText(user.getBio());

        Picasso.get()
                .load(user.getProfileImage())
                .into(profileImageView);
    }

    private void initializeViewModel() {
        userProfileEditViewModel = ViewModelProviders.of(this).get(UserProfileEditViewModel.class);
        userProfileEditViewModel.init();

        userProfileEditViewModel.getResponse().observe(this, response -> {

//            progressBarLayout.setVisibility(View.GONE);

            if (response.getStatus().equals("success")) {
                finish();
            }
        });
    }

    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("CA")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update_button) {
            String firstName = firstNameTextInputLayout.getEditText().getText().toString();
            String lastName = lastNameTextInputLayout.getEditText().getText().toString();
            String bio = bioTextInputLayout.getEditText().getText().toString();
            String addressJson = new Gson().toJson(address);
            userProfileEditViewModel.updateUserProfileApiCall(firstName, lastName, bio, addressJson);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String fullAddress = place.getAddress();
                addressTextInputLayout.getEditText().setText(fullAddress);

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
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(UserProfileEditActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                Log.i("UserProfileEditActivity", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}