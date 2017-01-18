package com.jkkniugmail.rubel.tourfriend.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by islan on 12/24/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //database
    public static final String DATABASE_NAME = "tour_friend";
    public static final int DATABASE_VERSION = 1;

    //user table information
    public static final String USER_TABLE = "user";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_FIRST_NAME = "first_name";
    public static final String COL_USER_LAST_NAME = "last_name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PHONE_NO = "phone_no";
    public static final String COL_USER_PASSWORD = "password";

    //event table information
    public static final String EVENT_TABLE = "event";
    public static final String COL_EVENT_ID = "id";
    public static final String COL_EVENT_TITLE = "title";
    public static final String COL_EVENT_DESCRIPTION = "description";
    public static final String COL_EVENT_LOCATION = "location";
    public static final String COL_EVENT_START_DATE = "start_date";
    public static final String COL_EVENT_BUDGET = "budget";
    public static final String COL_EVENT_USER_ID = "user_id";

    //expense table information
    public static final String EXPENSE_TABLE = "expense";
    public static final String COL_EXPENSE_ID = "id";
    public static final String COL_EXPENSE_DETAIL = "detail";
    public static final String COL_EXPENSE_COST = "cost";
    public static final String COL_EXPENSE_DATE_TIME = "date_time";
    public static final String COL_EXPENSE_EVENT_ID = "event_id";

    //moment table information
    public static final String MOMENT_TABLE = "moment";
    public static final String COL_MOMENT_ID = "id";
    public static final String COL_MOMENT_CAPTION = "caption";
    public static final String COL_MOMENT_DATE_TIME = "date_time";
    public static final String COL_MOMENT_IMAGE_LINK = "image_link";
    public static final String COL_MOMENT_EVENT_ID = "event_id";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_user_table = "CREATE TABLE " + USER_TABLE + "(" +
                COL_USER_ID + " INTEGER PRIMARY KEY," +
                COL_USER_FIRST_NAME + " TEXT," +
                COL_USER_LAST_NAME + " TEXT," +
                COL_USER_PHONE_NO + " TEXT," +
                COL_USER_EMAIL + " TEXT NOT NULL UNIQUE," +
                COL_USER_PASSWORD + " TEXT )";

        String sql_event_table = "CREATE TABLE " + EVENT_TABLE + "(" +
                COL_EVENT_ID + " INTEGER PRIMARY KEY, " +
                COL_EVENT_TITLE + " TEXT, " +
                COL_EVENT_DESCRIPTION + " TEXT, " +
                COL_EVENT_LOCATION + " TEXT, " +
                COL_EVENT_START_DATE + " TEXT, " +
                COL_EVENT_BUDGET + " NUMBER, " +
                COL_EVENT_USER_ID + " INTEGER, " +
                " FOREIGN KEY(" + COL_EVENT_USER_ID + " ) REFERENCES "
                + USER_TABLE + " ( " + COL_USER_ID + " ) ON DELETE CASCADE ON UPDATE CASCADE ) ";

        String sql_expense_table = "CREATE TABLE " + EXPENSE_TABLE + "(" +
                COL_EXPENSE_ID + " INTEGER PRIMARY KEY, " +
                COL_EXPENSE_DETAIL + " TEXT, " +
                COL_EXPENSE_COST + " NUMBER, " +
                COL_EXPENSE_DATE_TIME + " TEXT, " +
                COL_EXPENSE_EVENT_ID + " INTEGER, " +
                " FOREIGN KEY(" + COL_EXPENSE_EVENT_ID + " ) REFERENCES "
                + EVENT_TABLE + " ( " + COL_EVENT_ID + " ) ON DELETE CASCADE ON UPDATE CASCADE ) ";

        String sql_moment_table = "CREATE TABLE " + MOMENT_TABLE + "(" +
                COL_MOMENT_ID + " INTEGER PRIMARY KEY, " +
                COL_MOMENT_IMAGE_LINK + " TEXT NOT NULL, " +
                COL_MOMENT_CAPTION + " TEXT, " +
                COL_MOMENT_DATE_TIME + " TEXT, " +
                COL_MOMENT_EVENT_ID + " INTEGER, " +
                " FOREIGN KEY(" + COL_MOMENT_EVENT_ID + " ) REFERENCES "
                + EVENT_TABLE + " ( " + COL_EVENT_ID + " ) ON DELETE CASCADE ON UPDATE CASCADE ) ";

        db.execSQL(sql_user_table);
        db.execSQL(sql_event_table);
        db.execSQL(sql_expense_table);
        db.execSQL(sql_moment_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
