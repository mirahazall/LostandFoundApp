package com.example.lostandfoundapp;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ShowOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_on_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap map) {
        googleMap = map;

       /* List<String> typeList = getAllLTypeDataFromDatabase();
        List<String> nameList = getAllLNameDataFromDatabase();
        String markerTitle = typeList + " " + nameList;
        */

        // Retrieve all location data from the database
        List<String> locationList = getAllLocationDataFromDatabase();

        // Iterate through the list of location data and add markers to the map
        for (String locationData : locationList) {
            // Split the location data string to separate latitude and longitude values
            String[] parts = locationData.split(",");
            if (parts.length == 2) {
                double latitude = Double.parseDouble(parts[0]);
                double longitude = Double.parseDouble(parts[1]);

                // Create a LatLng object
                LatLng location = new LatLng(latitude, longitude);

                // Add a marker to the map at the specified location
                googleMap.addMarker(new MarkerOptions().position(location));
            } else {
                Log.e(TAG, "Invalid location data format: " + locationData);
            }
        }
    }


    private List<String> getAllLocationDataFromDatabase() {

        List<String> locationList = new ArrayList<>();

        // Create an instance of DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LostAndFoundContract.COLUMN_LOCATION // The column containing location data
        };

        // Perform a query on the database to retrieve all rows
        Cursor cursor = db.query(
                LostAndFoundContract.TABLE_NAME, // The table to query
                projection,        // The columns to return
                null,              // The columns for the WHERE clause
                null,              // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                null               // don't specify sort order
        );

        // Check if the cursor contains data
        if (cursor != null && cursor.moveToFirst()) {
            // Iterate through the cursor to retrieve all location data
            do {
                String locationData = cursor.getString(cursor.getColumnIndexOrThrow(LostAndFoundContract.COLUMN_LOCATION));
                locationList.add(locationData);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database connection
        db.close();

        return locationList;
    }

/*
    private List<String> getAllLTypeDataFromDatabase() {
        List<String> typeList = new ArrayList<>();

        // Create an instance of DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which column(s) you want to retrieve
        String[] projection = {
                LostAndFoundContract.COLUMN_TYPE // The column containing location data
        };

        // Perform a query on the database to retrieve all rows
        Cursor cursor = db.query(
                LostAndFoundContract.TABLE_NAME, // The table to query
                projection,        // The columns to return
                null,              // The columns for the WHERE clause
                null,              // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                null               // don't specify sort order
        );

        // Check if the cursor contains data
        if (cursor != null && cursor.moveToFirst()) {
            // Iterate through the cursor to retrieve all location data
            do {
                String typeData = cursor.getString(cursor.getColumnIndexOrThrow(LostAndFoundContract.COLUMN_TYPE));
                typeList.add(typeData);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database connection
        db.close();

        return typeList;
    }

    private String getAllLNameDataFromDatabase(){
        List<String> nameList = new ArrayList<>();

        // Create an instance of DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which column(s) you want to retrieve
        String[] projection = {
                LostAndFoundContract.COLUMN_NAME // The column containing location data
        };

        // Perform a query on the database to retrieve all rows
        Cursor cursor = db.query(
                LostAndFoundContract.TABLE_NAME, // The table to query
                projection,        // The columns to return
                null,              // The columns for the WHERE clause
                null,              // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                null               // don't specify sort order
        );

        // Check if the cursor contains data
        if (cursor != null && cursor.moveToFirst()) {
            // Iterate through the cursor to retrieve all location data
            do {
                String nameData = cursor.getString(cursor.getColumnIndexOrThrow(LostAndFoundContract.COLUMN_NAME));
                nameList.add(nameData);
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database connection
        db.close();

        return nameList;
    }

 */

}
