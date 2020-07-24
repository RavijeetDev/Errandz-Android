package com.wmdd.errandz.address;

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
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.wmdd.errandz.R;
import com.wmdd.errandz.data.Prefs;
import com.wmdd.errandz.hirerHome.HirerHomeActivity;
import com.wmdd.errandz.taskerHomeScreen.TaskerHomeActivity;
import com.wmdd.errandz.userProfileEdit.UserProfileEditActivity;
import com.wmdd.errandz.userProfileEdit.UserProfileEditViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;
    private com.wmdd.errandz.bean.Address address;

    private TextInputLayout addressTextInputLayout;
    private Button addAddressButton;

    private AddressViewModel addressViewModel;

    private GoogleMap myMap;
    //    private LatLngBounds CANADA = new LatLngBounds(
//            new LatLng(-44, 113), new LatLng(-10, 154));
    private LatLng CANADA = new LatLng(56.1304, -106.3468);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        addressTextInputLayout = findViewById(R.id.set_address_text_input_layout);
        addAddressButton = findViewById(R.id.select_address);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initializeAddressClickListener();
        initializeViewModel();

    }

    private void initializeAddressClickListener() {
        addressTextInputLayout.setOnClickListener(this);
        addressTextInputLayout.getEditText().setOnClickListener(this);
        addAddressButton.setOnClickListener(this);
    }

    private void initializeViewModel() {
        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        addressViewModel.init();
        addressViewModel.getResponse().observe(this, response -> {

//            progressBarLayout.setVisibility(View.GONE);

            if (response.getStatus().equals("success")) {
                Prefs.getInstance().saveFullAddress(address.getFullAddress());

                if(Prefs.getInstance().getUserType() == 1){
                    Intent intent = new Intent(AddressActivity.this, HirerHomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(AddressActivity.this, TaskerHomeActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_address_text_input_layout:
            case R.id.set_address_text_input_edit_text:
                onSetAddressCalled();
                break;
            case R.id.select_address:
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
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(49.1402515, -122.8369358))
//                .title("Marker"));
        myMap = googleMap;
        myMap.moveCamera(CameraUpdateFactory.newLatLng(CANADA));
        myMap.setMinZoomPreference(2.0f);
        myMap.setMaxZoomPreference(2.0f);
    }

    private void setUpMap(double latitude, double longitude) {
        LatLng position = new LatLng(latitude, longitude);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        myMap.addMarker(new MarkerOptions()
                .position(position));
        myMap.setMinZoomPreference(13.0f);
        myMap.setMaxZoomPreference(13.0f);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String fullAddress = place.getAddress();
//                addressTextInputLayout.getEditText().setText(fullAddress);

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
                    //setMarker(latLng);
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(AddressActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void setAddressValue() {
        addressTextInputLayout.getEditText().setText(address.getFullAddress());
    }


}