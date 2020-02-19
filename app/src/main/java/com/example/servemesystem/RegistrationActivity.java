package com.example.servemesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    DatabaseHelper db;

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phoneNumber;
    EditText username;
    EditText password;
    EditText confirmPassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        db = new DatabaseHelper(this);
        firstName = findViewById(R.id.edittext_firstName);
        lastName = findViewById(R.id.edittext_lastName);
        email = findViewById(R.id.edittext_email);
        phoneNumber = findViewById(R.id.edittext_phonenumber);
        username = findViewById(R.id.edittext_username);
        password = findViewById(R.id.edittext_password);
        confirmPassword = findViewById(R.id.edittext_cnf_password);
        register = findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(checkDataEntered()){
                    Boolean insert = db.insertUser(firstName.getText().toString(),
                                                   lastName.getText().toString(),
                                                   email.getText().toString(),
                                                   phoneNumber.getText().toString(),
                                                   username.getText().toString(),
                                                   password.getText().toString(),
                                                   "customer");
                    if(insert){
                        Toast.makeText(getApplicationContext(), "User Registered Succcessfully", Toast.LENGTH_SHORT).show();

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                finish();
                               redirectLogin();
                            }
                        }, 1500);
                    }
                }
            }
        });
    }

    void redirectLogin(){
        Intent registerIntent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(registerIntent);
    }

    boolean isOfType(EditText text, String type) {

        CharSequence input = text.getText().toString();
        if(type == "email")
            return (!TextUtils.isEmpty(input) && Patterns.EMAIL_ADDRESS.matcher(input).matches());
        else if (type == "phone")
            return(!TextUtils.isEmpty(input) && Patterns.PHONE.matcher(input).matches());
        else if (type == "username")
            return(!TextUtils.isEmpty(input) && ((String) input).matches("^[a-z0-9A-Z]{6,10}$"));
        else if(type == "password")
            return(!TextUtils.isEmpty(input) && ((String) input).matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"));
        else if(type == "confirmPassword")
            return(!TextUtils.isEmpty(input) && input != password);
        return false;
    };

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered(){
        boolean valid = true;
        if(isEmpty(firstName)){
            firstName.setError("First Name is required");
            valid = false;
        }
        if(isEmpty(lastName)){
            lastName.setError("Last Name is required");
            valid = false;
        }
        if(isEmpty(email)){
            email.setError("Last Name is required");
            valid = false;
        }
        else if(!isOfType(email,"email")){
            email.setError("Enter valid email!");
            valid = false;
        }
        if(isEmpty(phoneNumber)){
            phoneNumber.setError("Last Name is required");
            valid = false;
        }
        else if(!isOfType(phoneNumber,"phone")){
            phoneNumber.setError("Enter valid phone number!");
            valid = false;
        }
        if(isEmpty(username)){
            username.setError("Username is required");
            valid = false;
        }
        else if(!isOfType(username,"username")){
            username.setError("Username should be of the designated pattern");
            valid = false;
        }
        if(isEmpty(password)){
            password.setError("Password is required");
            valid = false;
        }
        else if(!isOfType(password,"password")){
            password.setError("Password should be of the designated pattern");
            valid = false;
        }
        if(isEmpty(confirmPassword)){
            confirmPassword.setError("Confirm Password is required");
            valid = false;
        }
        else if(!isOfType(confirmPassword,"confirmPassword")){
            confirmPassword.setError("Confirm password should match Password");
            valid = false;
        }
        if(!db.checkUser(username.getText().toString())){
            Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }
}
