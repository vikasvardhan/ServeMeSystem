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

import com.google.android.material.textfield.TextInputLayout;

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
    TextInputLayout fNameText;
    TextInputLayout lNameText;
    TextInputLayout emailText;
    TextInputLayout phoneText;
    TextInputLayout uNameText;
    TextInputLayout pwdText;
    TextInputLayout cnfPwdText;
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
        fNameText = findViewById(R.id.firstTxt);
        lNameText = findViewById(R.id.lstTxt);
        emailText = findViewById(R.id.emailtxt);
        phoneText = findViewById(R.id.phonetxt);
        uNameText = findViewById(R.id.usrnameTxt);
        pwdText = findViewById(R.id.passwordText);
        cnfPwdText = findViewById(R.id.cnfpasswordText);
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
        if(type == "name")
            return(!TextUtils.isEmpty(input) &&  ((String) input).matches("^[a-zA-Z]*$"));
        else if(type == "email")
            return (!TextUtils.isEmpty(input) && Patterns.EMAIL_ADDRESS.matcher(input).matches());
        else if (type == "phone")
            return(!TextUtils.isEmpty(input) && Patterns.PHONE.matcher(input).matches() && ((String) input).length() == 10);
        else if (type == "username")
            return(!TextUtils.isEmpty(input) && ((String) input).matches("^[a-z0-9A-Z]{6,15}$"));
        else if(type == "password")
            return(!TextUtils.isEmpty(input) && ((String) input).matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"));
        else if(type == "confirmPassword")
            return(!TextUtils.isEmpty(input) && input.equals(password.getText().toString()));
        return false;
    };

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered(){
        boolean valid = true;
        fNameText.setError(null);
        if(isEmpty(firstName)){
            fNameText.setError("\u2022 First Name is required");
            valid = false;
        }
        else if(!isOfType(firstName, "name")){
            fNameText.setError("\u2022 First Name must contain only alphabets");
        }
        lNameText.setError(null);
        if(isEmpty(lastName)){
            lNameText.setError("\u2022 Last Name is required");
            valid = false;
        }
        else if(!isOfType(lastName, "name")){
            lNameText.setError("\u2022 Last Name must contain only alphabets");
        }
        emailText.setError(null);
        if(isEmpty(email)){
            emailText.setError("\u2022 Email is required");
            valid = false;
        }
        else if(!isOfType(email,"email")){
            emailText.setError("\u2022 Enter a valid email! (###@###.com)");
            valid = false;
        }
        phoneText.setError(null);
        if(isEmpty(phoneNumber)){
            phoneText.setError("\u2022 Phone number is required");
            valid = false;
        }
        else if(!isOfType(phoneNumber,"phone")){
            phoneText.setError("\u2022 Enter a valid phone number! \n" +
                    "\u2022 Phone number should contain 10 numbers");
            valid = false;
        }
        uNameText.setError(null);
        if(isEmpty(username)){
            uNameText.setError("\u2022 Username is required");
            valid = false;
        }
        else if(!isOfType(username,"username")){
            uNameText.setError("\u2022 Username should be of 6 to 15 characters in length \n" +
                    "\u2022 Username should be alphanumeric");
            valid = false;
        }
        pwdText.setError(null);
        if(isEmpty(password)){
            pwdText.setError("Password is required");
            valid = false;
        }
        else if(!isOfType(password,"password")){
            pwdText.setError("\u2022 Password should have minimum 8 characters \n" +
                    "\u2022 Should have at least one Upper Case letter \n" +
                    "\u2022 Should have at least one Lower Case letter \n" +
                    "\u2022 Should have at least one Number \n" +
                    "\u2022 Should have at least one special character");
            valid = false;
        }
        cnfPwdText.setError(null);
        if(isEmpty(confirmPassword)){
            cnfPwdText.setError("Confirm Password is required");
            valid = false;
        }
        else if(!isOfType(confirmPassword,"confirmPassword")){
            cnfPwdText.setError("Confirm password should match Password");
            valid = false;
        }
        if(!db.checkUser(username.getText().toString())){
            Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }
}
