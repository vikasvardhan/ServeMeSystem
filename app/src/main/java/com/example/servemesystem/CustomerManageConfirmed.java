package com.example.servemesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class CustomerManageConfirmed extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseAccess db;
    List<ServiceRequest> confirmedRequests;
    ListCustConfirmedServiceRequest mListDataAdapter;
    SharedPreferences sharedpreferences;
    ServiceRequest currConfirmedRequest;
    CustomerManageConfirmed mFrame;

    public CustomerManageConfirmed() {
        mFrame = this;
        // Required empty public constructor
    }

    public static CustomerManageConfirmed newInstance(String param1, String param2) {
        CustomerManageConfirmed fragment = new CustomerManageConfirmed();
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
        View view = inflater.inflate(R.layout.fragment_customer_manage_confirmed,
                                    container,
                                    false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Confirmed Requests");

        ListView lvItems = (ListView) view.findViewById (R.id.listView_customer_confirmed_requests);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
            }
        });

        sharedpreferences = getActivity().getSharedPreferences(FirstFragment.PREFERENCES,
                Context.MODE_PRIVATE);

        db = DatabaseAccess.getInstance(getActivity());

        int userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        if(userId != -1){
            confirmedRequests = db.getConfirmedRequestsForCustomer(userId);
            mListDataAdapter = new ListCustConfirmedServiceRequest(getContext(),
                                                                    R.layout.row_customer_confirmed_request,
                                                                    confirmedRequests,
                                                                    mFrame);
            lvItems.setAdapter(mListDataAdapter);
        }
        return view;
    }

    public void cancelServiceRequest(int position){
        currConfirmedRequest
                = (ServiceRequest) confirmedRequests.get(position);
        String username
                = sharedpreferences.getString(UserAccount.USERNAME, "");
        if(!username.equals(""))
        {
            if(!username.equals(""))
            {
                UserAccount uc = db.getAccount(username);
                int numPoints = uc.getPoints();
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getContext());
                confirmBuilder.setMessage("You will lose "
                        + Integer.toString(numPoints) + " points. Continue?");
                confirmBuilder.setCancelable(true);

                confirmBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String un = sharedpreferences.getString(UserAccount.USERNAME,
                                        "");
                                UserAccount user = db.getAccount(un);
                                // penalize customer
                                db.setCustomerPoints(user.getUserId(), 0);
                                db.customer_cancelServiceRequest(currConfirmedRequest.getServiceId());

                                // refresh list
                                confirmedRequests
                                        = db.getConfirmedRequestsForCustomer(user.getUserId());
                                mListDataAdapter.setList(confirmedRequests);
                                dialog.cancel();
                            }
                        });

                confirmBuilder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = confirmBuilder.create();
                alert11.show();
            }
        }
    }
}
