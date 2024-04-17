package com.example.lostandfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lost_found_database";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database tables
        db.execSQL(LostAndFoundContract.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + LostAndFoundContract.TABLE_NAME);
        // Recreate the database
        onCreate(db);
    }

    public long insertData(String type, String name, String phone, String description, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LostAndFoundContract.COLUMN_TYPE, type);
        values.put(LostAndFoundContract.COLUMN_NAME, name);
        values.put(LostAndFoundContract.COLUMN_PHONE, phone);
        values.put(LostAndFoundContract.COLUMN_DESCRIPTION, description);
        values.put(LostAndFoundContract.COLUMN_DATE, date);
        values.put(LostAndFoundContract.COLUMN_LOCATION, location);
        long newRowId = db.insert(LostAndFoundContract.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public List<LostandFoundItem> getAllItems() {
        List<LostandFoundItem> lostAndFoundItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    LostAndFoundContract.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    LostandFoundItem item = cursorToItem(cursor);
                    lostAndFoundItems.add(item);
                    cursor.moveToNext();
                }
            }
        } finally {
            // Close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        Log.d("DatabaseHelper", "Number of items retrieved: " + lostAndFoundItems.size());
        return lostAndFoundItems;
    }

    private LostandFoundItem cursorToItem(Cursor cursor) {
        // Retrieve column indices
        int idIndex = cursor.getColumnIndex(LostAndFoundContract.COLUMN_ID);
        int typeIndex = cursor.getColumnIndex(LostAndFoundContract.COLUMN_TYPE);
        int nameIndex = cursor.getColumnIndex(LostAndFoundContract.COLUMN_NAME);
        int phoneIndex = cursor.getColumnIndex(LostAndFoundContract.COLUMN_PHONE);
        int descriptionIndex = cursor.getColumnIndex(LostAndFoundContract.COLUMN_DESCRIPTION);
        int dateIndex = cursor.getColumnIndex(LostAndFoundContract.COLUMN_DATE);
        int locationIndex = cursor.getColumnIndex(LostAndFoundContract.COLUMN_LOCATION);

        // Check if column indices are valid
        if (idIndex != -1 && typeIndex != -1 && nameIndex != -1 && phoneIndex != -1 && descriptionIndex != -1 && dateIndex != -1 && locationIndex != -1) {
            //retrieve column values
            long id = cursor.getLong(idIndex);
            String type = cursor.getString(typeIndex);
            String name = cursor.getString(nameIndex);
            String phone = cursor.getString(phoneIndex);
            String description = cursor.getString(descriptionIndex);
            String date = cursor.getString(dateIndex);
            String location = cursor.getString(locationIndex);

            // Create and return a LostandFoundItem object
            return new LostandFoundItem(id, type, name, phone, description, date, location);
        } else {
            // Handle case where one or more columns are not found
            return null;
        }
    }

    public LostandFoundItem getItemById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        LostandFoundItem item = null;

        // Define the columns to retrieve
        String[] projection = {
                LostAndFoundContract.COLUMN_ID,
                LostAndFoundContract.COLUMN_TYPE,
                LostAndFoundContract.COLUMN_NAME,
                LostAndFoundContract.COLUMN_PHONE,
                LostAndFoundContract.COLUMN_DESCRIPTION,
                LostAndFoundContract.COLUMN_DATE,
                LostAndFoundContract.COLUMN_LOCATION
        };

        // Define the selection criteria
        String selection = LostAndFoundContract.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        // Query the database
        Cursor cursor = db.query(
                LostAndFoundContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // If the cursor contains a row, extract the item details
        if (cursor != null && cursor.moveToFirst()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow(LostAndFoundContract.COLUMN_TYPE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(LostAndFoundContract.COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(LostAndFoundContract.COLUMN_PHONE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(LostAndFoundContract.COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(LostAndFoundContract.COLUMN_DATE));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(LostAndFoundContract.COLUMN_LOCATION));

            // Create a LostandFoundItem object with the retrieved details
            item = new LostandFoundItem(id, type, name, phone, description, date, location);
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return item;
    }

    public boolean deleteItem(long itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = LostAndFoundContract.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };
        int deletedRows = db.delete(LostAndFoundContract.TABLE_NAME, selection, selectionArgs);
        db.close();
        return deletedRows > 0;
    }

}

