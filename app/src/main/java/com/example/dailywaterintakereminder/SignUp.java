package com.example.dailywaterintakereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    DataBaseHelper myDb;
    EditText editName, editAge, editWeight, editUsername, editPassword;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        //initialize the database
        myDb = new DataBaseHelper(this);

        editName = findViewById(R.id.textInputEditName);
        editAge = findViewById(R.id.editTextAge);
        editWeight = findViewById(R.id.editTextWeight);
        editUsername = findViewById(R.id.editTextUsername);
        editPassword = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.registerBtn);

        //call registerData function the function
        registerData();

    }

    //Insert records in a SQLite database
    public void registerData(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String age = editAge.getText().toString().trim();
                String weight = editWeight.getText().toString().trim();
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                // Validate name
                if(TextUtils.isEmpty(name)){
                    editName.setError("Name is required");
                    editName.requestFocus();
                    return;
                }

                // Validate age
                if(TextUtils.isEmpty(age)){
                    editAge.setError("Age is required");
                    editAge.requestFocus();
                    return;
                }

                // Validate weight
                if(TextUtils.isEmpty(weight)){
                    editWeight.setError("Weight is required");
                    editWeight.requestFocus();
                    return;
                }

                // Validate username
                if(TextUtils.isEmpty(username)){
                    editUsername.setError("Username is required");
                    editUsername.requestFocus();
                    return;
                }

                // Validate password
                if(TextUtils.isEmpty(password)){
                    editPassword.setError("Password is required");
                    editPassword.requestFocus();
                    return;
                }else if(!isValidPassword(password)){
                    editPassword.setError("Password should contain at least 8 characters including at least one uppercase letter, one lowercase letter, one digit and one special character.");
                    editPassword.requestFocus();
                    return;
                }

                boolean isInserted = myDb.insertData(name, Integer.valueOf(age), Integer.valueOf(weight),
                        username, password);
                if(isInserted == true) {
                    Toast.makeText(SignUp.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
                    openLogin();
                }else{
                    Toast.makeText(SignUp.this,"Data Not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Validate password pattern
    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }


    //function for open the loginpage
    public void openLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
