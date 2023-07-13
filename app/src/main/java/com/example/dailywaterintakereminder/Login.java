package com.example.dailywaterintakereminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    DataBaseHelper myDb;
    EditText editUsername, editPassword;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //initialize the database
        myDb = new DataBaseHelper(this);

        editUsername = findViewById(R.id.editTextTextUsername);
        editPassword = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.loginButton);

        // check login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    editUsername.setError("Please enter your username");
                    editUsername.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editPassword.setError("Please enter your password");
                    editPassword.requestFocus();
                    return;
                }

                boolean loginSuccessful = myDb.login(username, password);
                if (loginSuccessful) {
                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // After Successful login opening the Dashboard
                    openDashboard();
                } else {
                    showMessage("Invalid username or password", "Don't you have an account please signup");
                }
            }
        });

        // Open the Signup page
        TextView registerPageButton = (TextView) findViewById(R.id.registerPageBtn);
        registerPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });
    }

    // open Dashboard
    public void openDashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    // open Signup
    public void openSignup() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    // Show Alert messages
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
