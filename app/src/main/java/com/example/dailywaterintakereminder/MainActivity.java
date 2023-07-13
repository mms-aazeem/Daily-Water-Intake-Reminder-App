package com.example.dailywaterintakereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Navigate to the LoginPage
        Button loginButton = (Button)findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        //Navigate to the Signup Page
        Button signupButton = (Button)findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });
    }


    //Login page Function
    public void openLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    //Signup page Function
    public void openSignup(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}