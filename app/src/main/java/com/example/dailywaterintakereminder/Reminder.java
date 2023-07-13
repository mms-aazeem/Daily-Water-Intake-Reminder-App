package com.example.dailywaterintakereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Reminder extends AppCompatActivity {

    private Spinner spinnerTimer1;
    private Button setReminderButton;
    private Handler handler;
    private Runnable soundRunnable;
    private MediaPlayer mediaPlayer;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Get a reference to the spinner
        spinnerTimer1 = findViewById(R.id.spinnerTimer1);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.timer_values_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerTimer1.setAdapter(adapter);

        setReminderButton = findViewById(R.id.setReminderButton);

        // Get the previously selected position from the SharedPreferences
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        int position = sharedPrefs.getInt("spinnerPosition", 0);
        spinnerTimer1.setSelection(position);

        // Set up the handler to play the sound every X minutes based on the selected spinner item
        handler = new Handler();
        soundRunnable = new Runnable() {
            @Override
            public void run() {
                // Play notification sound here
                // We can use the MediaPlayer class to play a sound file, or use Android's built-in notification system
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.reminder_alert);
                mediaPlayer.start();

                handler.postDelayed(this, getInterval());
            }
        };

        // Set up an OnClickListener for the setReminderButton
        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel any previously scheduled reminders
                handler.removeCallbacks(soundRunnable);

                // Get the selected item position from the spinner
                int position = spinnerTimer1.getSelectedItemPosition();

                // Save the selected position to SharedPreferences
                sharedPrefs.edit().putInt("spinnerPosition", position).apply();

                // Set up the handler to play the sound every X minutes based on the selected spinner item
                switch (position) {
                    case 0: // Every 15 minutes
                        handler.postDelayed(soundRunnable, 15 * 60 * 1000);
                        Toast.makeText(Reminder.this, "Next reminder after 15 minutes", Toast.LENGTH_LONG).show();
                        break;
                    case 1: // Every 30 minutes
                        handler.postDelayed(soundRunnable, 30 * 60 * 1000);
                        Toast.makeText(Reminder.this, "Next reminder after 30 minutes", Toast.LENGTH_LONG).show();
                        break;
                    case 2: // Every 45 minutes
                        handler.postDelayed(soundRunnable, 45 * 60 * 1000);
                        Toast.makeText(Reminder.this, "Next reminder after 45 minutes", Toast.LENGTH_LONG).show();
                        break;
                    case 3: // Every 1 hour
                        handler.postDelayed(soundRunnable, 60 * 60 * 1000);
                        Toast.makeText(Reminder.this, "Next reminder after 1 hour", Toast.LENGTH_LONG).show();
                        break;
                    case 4: // Every 2 hours
                        handler.postDelayed(soundRunnable, 2 * 60 * 60 * 1000);
                        Toast.makeText(Reminder.this, "Next reminder after 2 hours", Toast.LENGTH_LONG).show();
                        break;
                }

                // Save the selected spinner position to SharedPreferences
                saveSelectedInterval(position);
            }
        });

        // Retrieve the last selected spinner position from SharedPreferences and set the spinner to that position
        int lastSelectedPosition = getSelectedInterval();
        spinnerTimer1.setSelection(lastSelectedPosition);
    }

    //retrieve the selected time period from the Spinner
    private long getInterval() {
        int position = spinnerTimer1.getSelectedItemPosition();

        switch (position) {
            case 0: // Every 15 minutes
                return 15 * 60 * 1000;
            case 1: // Every 30 minutes
                return 30 * 60 * 1000;
            case 2: // Every 45 minutes
                return 45 * 60 * 1000;
            case 3: // Every 1 hour
                return 60 * 60 * 1000;
            case 4: // Every 2 hours
                return 2 * 60 * 60 * 1000;
            default:
                return 0;
        }
    }

    // This method plays the audio file from the raw folder named "reminder_alert.mp3"
    private void playAudioFile() {
        Uri audioUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.reminder_alert);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), audioUri);
        r.play();
    }

    // Save the selected interval position to SharedPreferences
    private void saveSelectedInterval(int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedInterval", position);
        editor.apply();
    }

    // Retrieve the last selected interval position from SharedPreferences
    private int getSelectedInterval() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("selectedInterval", 0); // default value is 0 (15 minutes)
    }

}