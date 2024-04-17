package com.example.lostandfoundapp;

public class LostAndFoundContract {
        public static final String TABLE_NAME = "lost_found_database";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LOCATION = "location";

        public static final String CREATE_TABLE_QUERY =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_TYPE + " TEXT," +
                        COLUMN_NAME + " TEXT," +
                        COLUMN_PHONE + " TEXT," +
                        COLUMN_DESCRIPTION + " TEXT," +
                        COLUMN_DATE + " DATE," +
                        COLUMN_LOCATION + " TEXT" +
                        ")";
}

