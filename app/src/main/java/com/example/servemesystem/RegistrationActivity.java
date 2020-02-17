package com.example.servemesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
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
                checkDataEntered();
            }
        });
    }

    boolean isOfType(EditText text, String type) {

        CharSequence input = text.getText().toString();
        if(type == "email")
            return (!TextUtils.isEmpty(input) && Patterns.EMAIL_ADDRESS.matcher(input).matches());
        else if (type == "phone")
            return(!TextUtils.isEmpty(input) && Patterns.PHONE.matcher(input).matches());
        else if (type == "username")
            return(!TextUtils.isEmpty(input) && !((String) input).matches("^[a-z0-9]{3,15}$"));
        else if(type == "password")
            return(!TextUtils.isEmpty(input) && !((String) input).matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$"));
        else if(type == "confirmPassword")
            return(!TextUtils.isEmpty(input) && input != password);
        return false;
    };

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkDataEntered(){
        if(isEmpty(firstName)){
            firstName.setError("First Name is required");
        }
        if(isEmpty(lastName)){
            lastName.setError("Last Name is required");
        }
        if(isEmpty(email)){
            email.setError("Last Name is required");
        }
        if(isEmpty(phoneNumber)){
            phoneNumber.setError("Last Name is required");
        }
        if(isEmpty(username)){
            username.setError("Last Name is required");
        }
        if(isEmpty(password)){
            password.setError("Last Name is required");
        }
        if(isEmpty(confirmPassword)){
            confirmPassword.setError("Last Name is required");
        }
        if(!isOfType(email,"email")){
            email.setError("Enter valid email!");
        }
        if(!isOfType(phoneNumber,"phone")){
            phoneNumber.setError("Enter valid phone number!");
        }
        if(!isOfType(username,"username")){
            username.setError("Username should be of the designated pattern");
        }
        if(!isOfType(password,"password")){
            password.setError("Password should be of the designated pattern");
        }
        if(!isOfType(confirmPassword,"confirmPassword")){
            confirmPassword.setError("Confirm password should match Password");
        }

    }
}
