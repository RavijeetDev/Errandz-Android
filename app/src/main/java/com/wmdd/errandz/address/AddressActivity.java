package com.wmdd.errandz.address;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.wmdd.errandz.R;
import com.wmdd.errandz.bean.User;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.databinding.ActivityAddressBinding;
import com.wmdd.errandz.hirerHome.HirerHomeActivity;
import com.wmdd.errandz.login.LoginFragment;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeActivity;
import com.wmdd.errandz.userProfileEdit.UserProfileEditActivity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.wmdd.errandz.util.Constants.FROM_ACTIVITY;

public class AddressActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;
    private LatLng CANADA = new LatLng(56.1304, -106.3468);

    private GoogleMap googleMap;

    private User user;
    private com.wmdd.errandz.bean.Address address;

    private AddressViewModel addressViewModel;
    private ActivityAddressBinding addressBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressBinding = DataBindingUtil.setContentView(this, R.layout.activity_address);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initializeAddressClickListener();
        initializeViewModel();

    }

    private void initializeAddressClickListener() {
        addressBinding.setAddressTextInputLayout.setOnClickListener(this);
        addressBinding.setAddressTextInputEditText.setOnClickListener(this);
        addressBinding.selectAddress.setOnClickListener(this);
    }

    private void initializeViewModel() {
        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        addressViewModel.init();
        addressViewModel.getResponse().observe(this, response -> {

            addressBinding.progressBarView.setVisibility(View.GONE);

            if (response.getStatus().equals("success")) {

                Prefs.getInstance().saveFullAddress(address.getFullAddress());
//                if (Prefs.getInstance().getUserBio().isEmpty() || Prefs.getInstance().getProfileImage().isEmpty()) {
//                    user = getIntent().getParcelableExtra("USER");
//
//                    if (user != null) {
//                        addressViewModel.getUserInfo();
//                    } else {
//                        openUserEditProfile();
//                    }
//                } else {
                    if (Prefs.getInstance().getUserType() == 1) {
                        Intent intent = new Intent(AddressActivity.this, HirerHomeActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AddressActivity.this, TaskerHomeActivity.class);
                        startActivity(intent);
                    }
//                }
                finish();
            }
        });

        addressViewModel.getUserMutableLiveData().observe(this, userInfo -> {
            if (userInfo != null) {
                addressBinding.progressBarView.setVisibility(View.GONE);
                user = userInfo;
                openUserEditProfile();
            }
        });
    }

    private void openUserEditProfile() {

        Intent intent = new Intent(this, UserProfileEditActivity.class);
        intent.putExtra(FROM_ACTIVITY, LoginFragment.class.getSimpleName());
        intent.putExtra("USER", user);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_address_text_input_layout:
            case R.id.set_address_text_input_edit_text:
                onSetAddressCalled();
                break;
            case R.id.select_address:
                addressBinding.progressBarView.setVisibility(View.VISIBLE);
                addressViewModel.callUserAddressApi(address);
                break;
        }
    }

    public void onSetAddressCalled() {

        String apiKey = getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(this);

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("CA")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(CANADA));
        this.googleMap.setMinZoomPreference(2.0f);
        this.googleMap.setMaxZoomPreference(2.0f);
    }

    private void setUpMap(double latitude, double longitude) {
        LatLng position = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.addMarker(new MarkerOptions()
                .position(position));
        googleMap.setMinZoomPreference(13.0f);
        googleMap.setMaxZoomPreference(13.0f);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String fullAddress = place.getAddress();
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

                        address = new com.wmdd.errandz.bean.Address();
                        address.setFullAddress(fullAddress);
                        address.setStreetAddress("" + house + " " + street);
                        address.setCity(city);
                        address.setProvince(state);
                        address.setPostalCode(postalCode);
                        address.setCountry(country);
                        address.setLatitude(String.valueOf(place.getLatLng().latitude));
                        address.setLongitude(String.valueOf(place.getLatLng().longitude));

                        setAddressValue();
                        setUpMap(place.getLatLng().latitude, place.getLatLng().longitude);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(AddressActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private void setAddressValue() {
        addressBinding.setAddressTextInputEditText.setText(address.getFullAddress());
    }


}