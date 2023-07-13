package com.example.dailywaterintakereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LetsDrinkSomeWater extends AppCompatActivity {
    DataBaseHelper myDb;
    EditText editWater;
    Button addWaterButton;
    TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_drink_some_water);

        //initialize the Database
        myDb = new DataBaseHelper(this);

        editWater = findViewById(R.id.editTextWaterLevel);
        addWaterButton = findViewById(R.id.addWaterLevelBtn);
        tableLayout = findViewById(R.id.tableLayout);

        //call the function to get the current time
        currentTime();

        // set onClickListener for the button to insert data to database
        addWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertWater();
            }
        });

        viewWaterLevel(tableLayout);
    }

    //To get the current time
    public String currentTime() {
        // create a SimpleDateFormat object with desired format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        // get the current date and time
        String currentTimeString = sdf.format(new Date());

        // find the TextView by its ID
        TextView editTime = findViewById(R.id.editCurrentTime);

        // set the text of the TextView to the current time string
        editTime.setText(currentTimeString);
        return currentTimeString;
    }

    private String currentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    //Insert the drunk water level to database
    public void insertWater() {
        String currentTimeString = currentTime();
        String dateString = currentDate();
        String waterLevel = editWater.getText().toString();

        // Validate username
        if(TextUtils.isEmpty(waterLevel)){
            editWater.setError("Water Level is required");
            editWater.requestFocus();
            return;
        }

        boolean isInserted = myDb.insertWater(currentTimeString, waterLevel);
        if (isInserted) {
            Toast.makeText(LetsDrinkSomeWater.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
            openDashboard();
        } else {
            Toast.makeText(LetsDrinkSomeWater.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
        }
    }




    public void viewWaterLevel(TableLayout tableLayout) {
        Cursor cursor = myDb.getAllData();

        if (cursor.getCount() == 0) {
            // If there are no records, show a message
            TextView emptyView = new TextView(this);
            emptyView.setText("No records found.");
            tableLayout.addView(emptyView);
        } else {
            // Add headers to the table
            TableRow header = new TableRow(this);
            header.setBackgroundColor(Color.parseColor("#2596be"));
            header.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            String[] columns = {"Time", "Water Level (ml)"};
            for (String column : columns) {
                TextView headerView = new TextView(this);
                headerView.setText(column);
                headerView.setPadding(30, 5, 20, 5);
                header.addView(headerView);
            }
            tableLayout.addView(header);

            // Add the records to the table
            while (cursor.moveToNext()) {
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                String time = cursor.getString(1);
                String waterLevel = cursor.getString(2);

                TextView timeView = new TextView(this);
                timeView.setText(time);
                timeView.setPadding(20, 5, 20, 5);
                row.addView(timeView);

                TextView waterLevelView = new TextView(this);
                waterLevelView.setText(waterLevel);
                waterLevelView.setPadding(150, 5, 20, 5);
                row.addView(waterLevelView);

                tableLayout.addView(row);
            }
        }
    }


    public void openDashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

}