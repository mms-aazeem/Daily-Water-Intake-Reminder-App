package com.example.dailywaterintakereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    private TextView textViewName;// Added TextView for displaying name
    private TextView textViewAge;// Added TextView for displaying age
    private TextView textViewWeight;// Added TextView for displaying weight
    private TextView textViewTimer; // Added TextView for displaying timer value

    private DataBaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewName = findViewById(R.id.textViewName);
        textViewAge = findViewById(R.id.textViewAge);
        textViewWeight = findViewById(R.id.textViewWeight);
        textViewTimer = findViewById(R.id.textViewTimer); // Initialize textViewTimer

        dbHelper = new DataBaseHelper(this);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Get the data from the user table
        getUserData();

        // Get the saved timer value and display it in the textViewTimer
        int selectedInterval = getSelectedInterval();
        displayTimerValue(selectedInterval);
    }

    //retrieve the user data from database
    private void getUserData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DataBaseHelper.COL_2, DataBaseHelper.COL_3, DataBaseHelper.COL_4};
        Cursor cursor = db.query(DataBaseHelper.TABLE_NAME_USER, projection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL_2));
            int age = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COL_3));
            int weight = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COL_4));

            textViewName.setText(name);
            textViewAge.setText(String.valueOf(age));
            textViewWeight.setText(String.valueOf(weight));
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    // Retrieve the last selected interval position from SharedPreferences
    private int getSelectedInterval() {
        return sharedPreferences.getInt("selectedInterval", 0); // default value is 0 (15 minutes)
    }

    // Display the timer value in the textViewTimer
    private void displayTimerValue(int position) {
        String[] timerValuesArray = getResources().getStringArray(R.array.timer_values_array);
        textViewTimer.setText(timerValuesArray[position]);
    }
}
