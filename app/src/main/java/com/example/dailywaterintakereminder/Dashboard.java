package com.example.dailywaterintakereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    private boolean ambientTemperatureIsAvailable;
    private Sensor ambientTemperatureSensor;
    private SensorEventListener ambientTemperatureListener;
    private SensorManager sensorManager;
    private TextView temperatureTextView;
    private ProgressBar waterProgressBar;
    private int dailyWaterIntakeGoal = 3000; // set the daily water intake goal to 3000 ml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // To view the current Temperature using Ambient Temperature Sensor
        temperatureTextView = findViewById(R.id.temperatureView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            ambientTemperatureListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float temperature = event.values[0];
                    temperatureTextView.setText(String.format("%.1f °C", temperature));
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // Do nothing
                }
            };
            ambientTemperatureIsAvailable = true;
        } else {
            ambientTemperatureIsAvailable = false;
            Toast.makeText(this, "Ambient temperature sensor is not available", Toast.LENGTH_LONG).show();
        }

        // To display the dynamic progress bar
        waterProgressBar = findViewById(R.id.progressBar3);
        updateWaterProgressBar();

        //Call the Open functions
        Button registerButton = findViewById(R.id.buyWaterBottleButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBuyWaterBottle();
            }
        });

        Button letsDrinkBtn = findViewById(R.id.letsDrinkButton);
        letsDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLetsDrinkSomeWater();
            }
        });

        Button profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        ImageButton logoutBtn = findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        ImageButton reminderBtn = findViewById(R.id.reminderButton);
        reminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReminder();
            }
        });
    }

    // To update the progress bar with the current water intake progress
    public void updateWaterProgressBar() {
        int currentWaterIntake = 0;
        DataBaseHelper myDb = new DataBaseHelper(this);
        Cursor cursor = myDb.getAllData();

        while (cursor.moveToNext()) {
            String waterLevel = cursor.getString(2);
            currentWaterIntake += Integer.parseInt(waterLevel);
        }

        waterProgressBar.setMax(dailyWaterIntakeGoal);
        waterProgressBar.setProgress(currentWaterIntake);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ambientTemperatureIsAvailable) {
            ambientTemperatureListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    calculateTemperature(event);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // Do nothing
                }
            };
            sensorManager.registerListener(ambientTemperatureListener, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ambientTemperatureIsAvailable) {
            sensorManager.unregisterListener(ambientTemperatureListener);
        }
    }

    // Calculate the temperature from ambient temperature sensor values
    private void calculateTemperature(SensorEvent event) {
        float temperature = event.values[0];
    // Display the temperature in the UI
        temperatureTextView.setText(String.format("%.1f °C", temperature));
    }

    // Open Function Start for all buttons
    public void openBuyWaterBottle() {
        Intent intent = new Intent(this, BuyWaterBottle.class);
        startActivity(intent);
    }

    public void openLetsDrinkSomeWater() {
        Intent intent = new Intent(this, LetsDrinkSomeWater.class);
        startActivity(intent);
    }

    public void openProfile() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear all previous activities
        startActivity(intent);
        finish(); // close current activity
    }

    public void openReminder() {
        Intent intent = new Intent(this, Reminder.class);
        startActivity(intent);
    }
}