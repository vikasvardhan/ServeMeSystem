package com.example.servemesystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateServiceBid#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateServiceBid extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ServiceRequest serviceRequest;
    SharedPreferences sharedpreferences;
    int userId;
    DatabaseAccess db;

    TextInputLayout amt;
    TextInputLayout hrs;
    TextInputLayout comments;

    EditText amtText;
    EditText hoursText;
    EditText commentsText;

    public CreateServiceBid() {
        // Required empty public constructor
    }

    public CreateServiceBid(ServiceRequest inServiceRequest) {
        this.serviceRequest = inServiceRequest;
    }

    public static CreateServiceBid newInstance(String param1, String param2) {
        CreateServiceBid fragment = new CreateServiceBid();
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
        // Inflate the layout for this fragment
        View view
                = inflater.inflate(R.layout.fragment_create_service_bid, container, false);
        sharedpreferences = getActivity().getSharedPreferences(FirstFragment.PREFERENCES,
                Context.MODE_PRIVATE);
        db = DatabaseAccess.getInstance(getActivity());
        userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = DatabaseAccess.getInstance(getActivity());

        amt = view.findViewById(R.id.create_service_bid_amountLbl);
        hrs = view.findViewById(R.id.create_service_bid_hoursLbl);
        comments = view.findViewById(R.id.create_service_commentsLbl);

        amtText = view.findViewById(R.id.create_service_bid_amount);
        hoursText = view.findViewById(R.id.create_service_bid_hours);
        commentsText = view.findViewById(R.id.create_service_bid_comments);

        Button cancelButton = view.findViewById(R.id.create_service_bid_cancelBtn);
        Button submitButton = view.findViewById(R.id.create_service_bid_submitBtn);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String amount = amtText.getText().toString();
                    String numHours = hoursText.getText().toString();
                    String comments = commentsText.getText().toString();

                    ServiceBid sb = new ServiceBid();
                    sb.setAmt(Double.parseDouble(amount));
                    sb.setHours(Integer.parseInt(numHours));
                    sb.setNotes(comments);
                    sb.setVendorId(userId);
                    sb.setServiceId(serviceRequest.getServiceId());

                    if (db.insertServiceBid(sb)) {
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new VendorManageServiceRequests())
                                .commit();
                    } else {
                        Toast.makeText(getContext(),
                                "Failed to create bid",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        if (amtText.getText().length() == 0) {
            amt.setError("\u2022 Amount is required");
            valid = false;
        }
        if (hoursText.getText().length() == 0) {
            hrs.setError("\u2022 Hours is required");
            valid = false;
        }
        if(commentsText.getText().length() == 0){
            comments.setError("Comments are required");
            valid = false;
        }
        return valid;
    }

    ;
}
