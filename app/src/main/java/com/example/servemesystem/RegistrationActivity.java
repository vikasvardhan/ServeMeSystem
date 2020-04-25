package com.example.servemesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.util.Timer;
import java.util.TimerTask;

public class RegistrationActivity extends AppCompatActivity {
    //Anushka did this
    DatabaseAccess db;
    //Edit Text Values
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phoneNumber;
    EditText address;
    EditText addressName;
    EditText city;
    EditText state;
    EditText pinCode;
    EditText username;
    EditText password;
    EditText confirmPassword;
    EditText sampleRate;
    EditText tasks;

    //Text View Values
    TextInputLayout fNameText;
    TextInputLayout lNameText;
    TextInputLayout emailText;
    TextInputLayout phoneText;
    TextInputLayout addressText;
    TextInputLayout cityText;
    TextInputLayout stateText;
    TextInputLayout pinCodeText;
    TextInputLayout uNameText;
    TextInputLayout pwdText;
    TextInputLayout cnfPwdText;
    TextInputLayout sampleRateText;
    TextInputLayout tasksText;
    Button register;

    ViewFlipper regVf;

    PlacesAutocompleteTextView placesAutocomplete;

    String UserType = "customer";
    String selectedService = "";

    private Spinner spinner;
    private static final String[] categories = {"Appliances", "Electrical", "Plumbing", "Home Cleaning", "Tutoring", "Packaging and Moving", "Computer Repair", "Home Repair and Painting", "Pest Control"};

    CardView cardCust;
    CardView cardVend;

    CheckBox consent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //DB
        db = DatabaseAccess.getInstance(getApplicationContext());

        //Edit Text Values
        firstName = findViewById(R.id.edittext_firstName);
        lastName = findViewById(R.id.edittext_lastName);
        email = findViewById(R.id.edittext_email);
        phoneNumber = findViewById(R.id.edittext_phonenumber);
        username = findViewById(R.id.edittext_username);
        password = findViewById(R.id.edittext_password);
        confirmPassword = findViewById(R.id.edittext_cnf_password);
        register = findViewById(R.id.button_register);
        address = findViewById(R.id.edittext_address);
        addressName = findViewById(R.id.edittext_address_name);
        city = findViewById(R.id.edittext_city);
        state = findViewById(R.id.edittext_state);
        pinCode = findViewById(R.id.edittext_pinCode);
        sampleRate = findViewById(R.id.edittext_sampleRate);
        tasks = findViewById((R.id.edittext_tasks));


        //Text View Values
        fNameText = findViewById(R.id.firstTxt);
        lNameText = findViewById(R.id.lstTxt);
        emailText = findViewById(R.id.emailtxt);
        phoneText = findViewById(R.id.phonetxt);
        uNameText = findViewById(R.id.usrnameTxt);
        pwdText = findViewById(R.id.passwordText);
        cnfPwdText = findViewById(R.id.cnfpasswordText);
        addressText = findViewById(R.id.addressTxt);
        cityText = findViewById(R.id.cityTxt);
        stateText = findViewById(R.id.stateTxt);
        pinCodeText = findViewById(R.id.pinCode);
        sampleRateText = findViewById((R.id.sampleRate));
        tasksText = findViewById((R.id.tasks));

        //View Flipper
        regVf = (ViewFlipper) findViewById(R.id.reg_flipper);

        spinner = (Spinner) findViewById(R.id.spinnerServices);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                selectedService = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        cardCust = findViewById(R.id.cardCust);
        cardVend = findViewById(R.id.cardVend);
        cardCust.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
        cardCust.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardCust.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
                cardVend.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_on_primary));
                UserType = "customer";

            }
        }));
        cardVend.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardVend.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_secondary));
                cardCust.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_on_primary));
                UserType = "vendor";
            }
        }));
//Auto Complete
        placesAutocomplete = findViewById(R.id.edittext_address);

        placesAutocomplete.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        placesAutocomplete.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(final PlaceDetails details) {
                                Log.d("test", "details " + details);
                                addressName.setText(details.name);
                                for (AddressComponent component : details.address_components) {
                                    for (AddressComponentType type : component.types) {
                                        switch (type) {
                                            case LOCALITY:
                                                city.setText(component.long_name);
                                                break;
                                            case ADMINISTRATIVE_AREA_LEVEL_1:
                                                state.setText(component.short_name);
                                                break;
                                            case POSTAL_CODE:
                                                pinCode.setText(component.long_name);
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(final Throwable failure) {
                                Log.d("test", "failure " + failure);
                            }
                        });
                    }
                });

       consent = (CheckBox)findViewById(R.id.checkbox_consent);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (checkDataEntered("credentials")) {
                    Boolean insert = db.insertUser(firstName.getText().toString(),
                            lastName.getText().toString(),
                            email.getText().toString(),
                            phoneNumber.getText().toString(),
                            username.getText().toString(),
                            password.getText().toString(),
                            UserType,
                            addressName.getText().toString(),
                            city.getText().toString(),
                            state.getText().toString(),
                            pinCode.getText().toString(),
                            selectedService,
                            sampleRate.getText().toString(),
                            tasks.getText().toString());
                    if (insert) {
                        Snackbar snackbar = Snackbar
                                .make(view, "\u2022 User Registered Succcessfully!", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
                        snackbar.show();

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

    void redirectLogin() {
        Intent registerIntent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(registerIntent);
    }

    boolean isOfType(EditText text, String type) {

        CharSequence input = text.getText().toString();
        if (type == "name")
            return (!TextUtils.isEmpty(input) && ((String) input).matches("^[a-zA-Z]*$"));
        else if (type == "email")
            return (!TextUtils.isEmpty(input) && Patterns.EMAIL_ADDRESS.matcher(input).matches());
        else if (type == "phone")
            return (!TextUtils.isEmpty(input) && Patterns.PHONE.matcher(input).matches() && ((String) input).length() == 10);
        else if (type == "username")
            return (!TextUtils.isEmpty(input) && ((String) input).matches("^[a-z0-9A-Z]{6,15}$"));
        else if (type == "password")
            return (!TextUtils.isEmpty(input) && ((String) input).matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"));
        else if (type == "confirmPassword")
            return (!TextUtils.isEmpty(input) && input.equals(password.getText().toString()));
        else if(type == "number"){
            return(!TextUtils.isEmpty(input) && ((String)input).matches("\\d+"));
        }
        return false;
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void directView(View v) {
        //0
        if (v.getTag().equals("userType")) {
            regVf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
            regVf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
            regVf.setDisplayedChild(1);

        } else if (v.getTag().equals("initials")) {//1
            if (checkDataEntered("initials")) {
                regVf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
                regVf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
                consent.setVisibility(View.INVISIBLE);
                if (UserType == "customer")
                    regVf.setDisplayedChild(4);
                else
                    regVf.showNext();

            }
        } else if (v.getTag().equals("address")) {//2
            if (checkDataEntered("address")) {
                regVf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
                regVf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
                regVf.showNext();
            }
        } else if (v.getTag().equals("services")) {
            if(checkDataEntered("services")){
                regVf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
                regVf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
                consent.setVisibility(View.VISIBLE);
                regVf.showNext();
            }
        } else if (v.getTag().equals("back") || v.getTag().equals("back_cred")) {
            regVf.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
            regVf.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
            if (v.getTag().equals("back_cred") && UserType.equals("customer"))
                regVf.setDisplayedChild(1);
            else
                regVf.showPrevious();
        }
    }

    boolean checkDataEntered(String type) {
        boolean valid = true;
        fNameText.setError(null);
        if (type == "initials") {
            if (isEmpty(firstName)) {
                fNameText.setError("\u2022 First Name is required");
                valid = false;
            } else if (!isOfType(firstName, "name")) {
                fNameText.setError("\u2022 First Name should contain only alphabets");
            }
            lNameText.setError(null);
            if (isEmpty(lastName)) {
                lNameText.setError("\u2022 Last Name is required");
                valid = false;
            } else if (!isOfType(lastName, "name")) {
                lNameText.setError("\u2022 Last Name should contain only alphabets");
            }
            emailText.setError(null);
            if (isEmpty(email)) {
                emailText.setError("\u2022 Email is required");
                valid = false;
            } else if (!isOfType(email, "email")) {
                emailText.setError("\u2022 Enter a valid email! (###@###.com)");
                valid = false;
            }
            phoneText.setError(null);
            if (isEmpty(phoneNumber)) {
                phoneText.setError("\u2022 Phone number is required");
                valid = false;
            } else if (!isOfType(phoneNumber, "phone")) {
                phoneText.setError("\u2022 Enter a valid phone number! \n" +
                        "\u2022 Phone number should contain 10 numbers");
                valid = false;
            }
        } else if (type.equals("address")) {
            addressText.setError(null);
            if (isEmpty(addressName)) {
                addressText.setError("\u2022 Address is required");
                valid = false;
            }
            cityText.setError(null);
            if (isEmpty(city)) {
                cityText.setError("\u2022 City is required");
                valid = false;
            }
            stateText.setError(null);
            if (isEmpty(state)) {
                stateText.setError("\u2022 State is required");
                valid = false;
            }
            pinCodeText.setError(null);
            if (isEmpty(pinCode)) {
                pinCodeText.setError("\u2022 Pincode is required");
                valid = false;
            }
        }
        else if(type.equals("services")){
            sampleRateText.setError(null);
            if(!isOfType(sampleRate,"number")){
                sampleRateText.setError("Rate has to be a number");
                valid = false;
            }
        }else {
            uNameText.setError(null);
            if (isEmpty(username)) {
                uNameText.setError("\u2022 Username is required");
                valid = false;
            } else if (!isOfType(username, "username")) {
                uNameText.setError("\u2022 6 to 15 characters in length \n" +
                        "\u2022 Alphanumeric");
                valid = false;
            }
            pwdText.setError(null);
            if (isEmpty(password)) {
                pwdText.setError("Password is required");
                valid = false;
            } else if (!isOfType(password, "password")) {
                pwdText.setError("\u2022 Minimum 8 characters \n" +
                        "\u2022 Upper Case letter \n" +
                        "\u2022 Lower Case letter \n" +
                        "\u2022 Number \n" +
                        "\u2022 Special character");
                valid = false;
            }
            cnfPwdText.setError(null);
            if (isEmpty(confirmPassword)) {
                cnfPwdText.setError("\u2022 Confirm Password is required");
                valid = false;
            } else if (!isOfType(confirmPassword, "confirmPassword")) {
                cnfPwdText.setError("\u2022 Confirm password should match Password");
                valid = false;
            }
            if (!db.checkUser(username.getText().toString())) {
                Snackbar snackbar = Snackbar
                        .make(this.getWindow().getDecorView().findViewById(android.R.id.content), "\u2022 User already exists!", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_secondary));
                snackbar.show();
                valid = false;
            }
            TextInputLayout consentChkBox = (TextInputLayout)findViewById(R.id.checkBox);
            consentChkBox.setError(null);
            if(UserType.equals("vendor") && !consent.isChecked()){
                consentChkBox.setError("\u2022 Vendor's must agree to pay 20% to ServeMe");
                valid = false;
            }
        }
        return valid;
    }
}
