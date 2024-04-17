package com.example.lostandfoundapp;

import static android.content.ContentValues.TAG;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;

public class CreateAdvertActivity extends AppCompatActivity {
    Button buttonSave;
    DatabaseHelper dbHelper;
    EditText editTextLocation;
    private PlacesClient placesClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        editTextLocation = findViewById(R.id.editTextLocation);

        Places.initialize(getApplicationContext(), "AIzaSyC16OS9CBpY3HWxd0ezpU9oBBgUm6uTb6o");
        // Initialize Places SDK
        placesClient = Places.createClient(this);

        // Set up autocomplete adapter
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteFragment != null) {
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG ));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Get the latitude and longitude of the selected location
                LatLng location = place.getLatLng();
                double latitude = location.latitude;
                double longitude = location.longitude;

                // Set the text to "latitude, longitude" format in the EditText
                String latLngText = latitude + ", " + longitude;
                editTextLocation.setText(latLngText);
            }

            @Override
            public void onError(@NonNull Status status) {
            }
        });

        } else {
            Log.e(TAG, "AutocompleteFragment is null");
        }

        buttonSave = findViewById(R.id.buttonSave);
        dbHelper = new DatabaseHelper(this);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToDatabase();
            }
        });

        Button buttonGetCurrentLocation = findViewById(R.id.buttonGetCurrentLocation);

        buttonGetCurrentLocation.setOnClickListener(v -> {
            // Check for location permission
            if (ContextCompat.checkSelfPermission(
                    CreateAdvertActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the current location
                getCurrentLocation();
            } else {
                // Request location permission
                ActivityCompat.requestPermissions(
                        CreateAdvertActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE
                );
            }
        });
        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place != null) {
                    LatLng location = place.getLatLng();
                    if (location != null) {
                        // Get latitude and longitude
                        double latitude = location.latitude;
                        double longitude = location.longitude;

                        // Set the text to "latitude, longitude" format in the EditText
                        String latLngText = latitude + ", " + longitude;
                        editTextLocation.setText(latLngText);
                    } else {
                        // Handle the case when location is null
                        Log.e(TAG, "Location is null");
                    }
                } else {
                    // Handle the case when place is null
                    Log.e(TAG, "Place is null");
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // Handle the case when there is an error in autocomplete activity
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e(TAG, "Error: " + status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // Handle the case when user cancels the operation
                Log.i(TAG, "User canceled the operation");
            }
        }
    }

    private void saveDataToDatabase() {
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextPhone = findViewById(R.id.editTextPhone);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextDate = findViewById(R.id.editTextDate);
        EditText editTextLocation = findViewById(R.id.editTextLocation);
        RadioGroup radioGroupPostType = findViewById(R.id.radioGroupPostType);

        // Get the ID of the selected radio button
        int selectedRadioButtonId = radioGroupPostType.getCheckedRadioButtonId();

        // Initialize a string to hold the selected post type
        String type = "";

        // Check if any radio button is selected
        if (selectedRadioButtonId != -1) {
            // Get the selected radio button
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            // Get the text of the selected radio button
            type = selectedRadioButton.getText().toString();
        }

        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String description = editTextDescription.getText().toString();
        String date = editTextDate.getText().toString();
        String location =  editTextLocation.getText().toString();

        long newRowId = dbHelper.insertData(type, name, phone, description, date, location);
        if (newRowId != -1) {
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                CreateAdvertActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, proceed with getting the location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        // Got last known location.
                        if (location != null) {
                            // Handle location
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            editTextLocation.setText(latitude + ", " + longitude);
                            Toast.makeText(
                                    CreateAdvertActivity.this,
                                    "Latitude: " + latitude + ", Longitude: " + longitude,
                                    Toast.LENGTH_LONG
                            ).show();
                        } else {
                            Toast.makeText(CreateAdvertActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Permission not granted, request it from the user
            ActivityCompat.requestPermissions(
                    CreateAdvertActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the current location
                getCurrentLocation();
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
