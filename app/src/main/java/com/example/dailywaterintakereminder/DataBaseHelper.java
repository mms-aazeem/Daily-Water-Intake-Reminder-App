package com.example.dailywaterintakereminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {
    //constants for a SQLite database
    public static final String DATABASE_NAME = "waterReminder.db";

    //Sign up Table
    public static final String TABLE_NAME_USER = "user";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "AGE";
    public static final String COL_4 = "WEIGHT";
    public static String COL_5 = "USERNAME";
    public static String COL_6 = "PASSWORD";

    //Water Table
    public static final String TABLE_NAME_WATER = "water_intake";
    public static final String COL_ID = "ID";
    public static final String COL_TIME = "TIME";
    public static final String COL_WATER_LEVEL = "WATER_LEVEL";

    //constructor
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_USER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, AGE NUMBER, WEIGHT DECIMAL, USERNAME TEXT, PASSWORD TEXT)");

        db.execSQL("create table " + TABLE_NAME_WATER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TIME DATETIME, WATER_LEVEL INTEGER)");
    }

    //drops the existing table (if it exists) by executing the DROP TABLE IF EXISTS SQL statement with the table name.
    // This is done to remove the old table and its data, so that a new table can be created with any necessary changes.
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WATER);
        onCreate(db);
    }

    //insert tasks data into database
    public boolean insertData(String name, Integer age, Integer weight, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,age);
        contentValues.put(COL_4,weight);
        contentValues.put(COL_5,username);
        contentValues.put(COL_6,password);
        long results = db.insert(TABLE_NAME_USER, null,contentValues);

        if(results == -1)
            return false;
        else
            return true;
    }

    //implementing a login functionality
    //database query to check if the provided username and password match any records in the database
    public boolean login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_USER, new String[]{COL_1},
                COL_5 + "=? AND " + COL_6 + "=?", new String[]{username, password},
                null, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    //insert the water into database
    public boolean insertWater(String time, String waterLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TIME, time); // Store the time in the database in its original format
        contentValues.put(COL_WATER_LEVEL, waterLevel);
        long results = db.insert(TABLE_NAME_WATER, null, contentValues);
        return results != -1;
    }

//    private long getTimeInMillis(String time) {
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//            Date date = format.parse(time);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            return calendar.getTimeInMillis();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }


    //get the water level for the current date only
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor results = db.rawQuery("select * from "+ TABLE_NAME_WATER+ " WHERE TIME LIKE '" + currentDate + "%'", null);
        return results;
    }



}

