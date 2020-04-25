package com.example.servemesystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    public static final String PREFERENCES = "Preferences" ;

    DatabaseAccess db;
    EditText username;
    EditText password;
    Button submit;

    SharedPreferences pref;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        Fragment curr = this;
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        username = (EditText) view.findViewById(R.id.editText_loginUsername);
        password = (EditText) view.findViewById(R.id.editText_loginPassword);
        submit = (Button) view.findViewById(R.id.button_submit);

        pref = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        db = DatabaseAccess.getInstance(getActivity()); // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validCredentials = db.authenticate(username.getText().toString(),
                        password.getText().toString());

                if(validCredentials){

                    Toast.makeText(getActivity(), "Login successful" , Toast.LENGTH_SHORT).show();

                    //check user type
                    UserAccount user = db.getAccount(username.getText().toString());
                    String userType = user.getUserType();

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt(UserAccount.USERID, user.getUserId());
                    editor.putString(UserAccount.USERNAME, user.getUsername());
                    editor.putString(UserAccount.USERTYPE, user.getUserType());
                    editor.commit();

                    if(userType.equals("customer")){
                        intent = new Intent(getActivity(), CustomerActivity.class);
                        startActivity(intent);
                    }
                    else if(userType.equals("admin")){
                        intent = new Intent(getActivity(), AdminActivity.class);
                        startActivity(intent);
                    }
                    else{
                        intent = new Intent(getActivity(), VendorHome.class);
                        startActivity(intent);
                        navController.navigate(R.id.vendorHome);
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Login unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    }
}
