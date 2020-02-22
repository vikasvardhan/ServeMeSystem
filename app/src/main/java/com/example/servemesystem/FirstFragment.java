package com.example.servemesystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    DatabaseAccess db;
    EditText username;
    EditText password;
    Button submit;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        username = (EditText) view.findViewById(R.id.editText_loginUsername);
        password = (EditText) view.findViewById(R.id.editText_loginPassword);
        submit = (Button) view.findViewById(R.id.button_submit);

        db = DatabaseAccess.getInstance(getActivity());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validCredentials = db.authenticate(username.getText().toString(),
                                                           password.getText().toString());
                if(validCredentials){
                    Toast.makeText(getActivity(), "Login successful" , Toast.LENGTH_SHORT).show();
                    //check user type
                    String userType = db.getAccountType(username.getText().toString());
                    if(userType.equals("customer")){
                        Intent settingsIntent = new Intent(getActivity(), CustomerHome.class);
                        startActivity(settingsIntent);
                    }
                    else{
                        Intent settingsIntent = new Intent(getActivity(), VendorHome.class);
                        startActivity(settingsIntent);
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), "Login unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.textView_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(registerIntent);
            }
        });

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

//        view.findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
//                startActivity(settingsIntent);
//            }
//        });
    }
}
