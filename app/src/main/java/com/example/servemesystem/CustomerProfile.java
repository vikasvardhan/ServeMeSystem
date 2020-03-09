package com.example.servemesystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences sharedpreferences;
    DatabaseAccess db;
    int userId;

    public CustomerProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerProfile newInstance(String param1, String param2) {
        CustomerProfile fragment = new CustomerProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view
                = inflater.inflate(R.layout.fragment_customer_profile, container, false);

        sharedpreferences
                = getActivity().getSharedPreferences(FirstFragment.PREFERENCES,Context.MODE_PRIVATE);

        TextView name = (TextView) view.findViewById(R.id.customer_profile_name);
        TextView email = (TextView) view.findViewById(R.id.customer_profile_email);
        TextView phone = (TextView) view.findViewById(R.id.customer_profile_phone);
        TextView points = (TextView) view.findViewById(R.id.customer_profile_points);
        TextView balance = (TextView) view.findViewById(R.id.customer_profile_balance);
        TextView location = (TextView) view.findViewById(R.id.customer_profile_location);

        db = DatabaseAccess.getInstance(getActivity());
        userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        UserAccount uc = db.getAccount(userId);

        if (uc != null) {
            name.setText(uc.getLastName() + ", " + uc.getFirstName());
            email.setText(uc.getEmail());
            phone.setText("+" + uc.getPhone());
            points.setText(String.valueOf(uc.getPoints()));
            balance.setText("$" + uc.getWalletAmt());
            String locationStr = uc.getAddress();
            if(locationStr != null){
                if(locationStr.equals("")){
                    locationStr = "No Address Provided";
                }
            }
            else{
                locationStr = "No Address Provided";
            }
            location.setText(locationStr);
        }

        // Inflate the layout for this fragment
        return view;
    }
}
