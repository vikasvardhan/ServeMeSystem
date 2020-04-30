package com.example.servemesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class VendorManageConfirmed extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseAccess db;
    List<ServiceRequest> confirmedRequests;
    ListVendConfirmedServiceRequest mListDataAdapter;
    SharedPreferences sharedpreferences;
    //    Button btn_markCompleted;
    int userId;

    int currentRequestPosition;
    ServiceRequest currentServiceRequest;
    VendorManageConfirmed mFrame;

    //User Accounts
    UserAccount customerUc;
    UserAccount vendorUc;
    UserAccount adminUc;

    // wallet
    Double customerWallet = 0.0;
    Double vendorWallet = 0.0;
    Double adminWallet = 0.0;
    View view;

    public VendorManageConfirmed() {
        // Required empty public constructor
        mFrame = this;
    }

    public static VendorManageConfirmed newInstance(String param1, String param2) {
        VendorManageConfirmed fragment = new VendorManageConfirmed();
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
        view = inflater.inflate(R.layout.fragment_vendor_manage_confirmed,
                container,
                false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Confirmed Requests");

        ListView lvItems = (ListView) view.findViewById(R.id.listView_vendor_confirmed_requests);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                currentRequestPosition = position;
                if (currentRequestPosition != -1) {
//                    btn_markCompleted.setEnabled(true);
                }
            }
        });

//        btn_markCompleted = (Button) view.findViewById(R.id.btn_markServiceRequestAsComplete);
        sharedpreferences = getActivity().getSharedPreferences(FirstFragment.PREFERENCES,
                Context.MODE_PRIVATE);

        db = DatabaseAccess.getInstance(getActivity());

        userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        if (userId != -1) {
            confirmedRequests = db.getConfirmedRequestsForVendor(userId);
            mListDataAdapter
                    = new ListVendConfirmedServiceRequest(getContext(),
                    R.layout.row_vendor_confirmed_request,
                    confirmedRequests,
                    mFrame);
            lvItems.setAdapter(mListDataAdapter);
        }
        return view;
    }

    public void markComplete(int position) {


        currentServiceRequest
                = (ServiceRequest) confirmedRequests.get(position);
        ServiceBid winningBid = currentServiceRequest.getWinningBid();

        Double serviceCost = winningBid.getAmt();

        vendorUc = db.getAccount(userId);
        vendorWallet = vendorUc.getWalletAmt();
        customerUc = db.getAccount(currentServiceRequest.getCustomerId());
        customerWallet = customerUc.getWalletAmt();
        adminUc = db.getAccount("admin");
        adminWallet = adminUc.getWalletAmt();

        Log.d("A (Before): ", adminWallet.toString());
        Log.d("C (Before): ", customerWallet.toString());
        Log.d("V (Before): ", vendorWallet.toString());
        //Calculate new values
        customerWallet = customerWallet - serviceCost;
        Double vendorPay = 0.8 * serviceCost;
        vendorWallet = (Double) (vendorWallet + vendorPay);
        adminWallet = (Double) (adminWallet + (0.2 * serviceCost));

        String msg = "C: " + Double.toString(customerWallet)
                + ", V:" + Double.toString(vendorWallet)
                + ", A:" + Double.toString(adminWallet);
        Log.d("Wallet figures: ", msg);
        Log.d("Vendor Pay", Double.toString(vendorPay));

        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity(),
                R.style.ThemeOverlay_MaterialComponents_Dialog);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView lbl1 = new TextView(getActivity());
        lbl1.setText("Bill Customer: ");
        lbl1.setWidth(275);
        TextView txt1 = new TextView(getActivity());
        txt1.setText("$" + Double.toString(serviceCost));
        txt1.setTextColor(Color.RED);

        LinearLayout row1 = new LinearLayout(getActivity());
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.setPadding(0, 20, 0, 10);
        row1.addView(lbl1);
        row1.addView(txt1);

        TextView lbl2 = new TextView(getActivity());
        lbl2.setText("Your Earnings: ");
        lbl2.setWidth(275);
        TextView txt2 = new TextView(getActivity());
        txt2.setText("$" + Double.toString(round(vendorPay, 2)));
        txt2.setTextColor(Color.GREEN);

        LinearLayout row2 = new LinearLayout(getActivity());
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setPadding(0, 10, 0, 10);
        row2.addView(lbl2);
        row2.addView(txt2);

        linearLayout.addView(row1);
        linearLayout.addView(row2);
        linearLayout.setPadding(60, 0, 60, 0);

        confirmBuilder.setTitle("Cost Breakdown");
        confirmBuilder.setView(linearLayout);


        confirmBuilder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // mark as complete
                        db.vendor_markServiceRequestAsComplete(currentServiceRequest.getServiceId());

                        // update wallets
                        db.updateWalletBalance(customerUc.getUserId(), round(customerWallet, 2));
                        db.updateWalletBalance(vendorUc.getUserId(), round(vendorWallet, 2));
                        db.updateWalletBalance(adminUc.getUserId(), round(adminWallet, 2));

                        // award customer 100 points
                        db.setCustomerPoints(customerUc.getUserId(), 100);

                        // request query for pending service requests
                        confirmedRequests = db.getConfirmedRequestsForVendor(userId);
                        mListDataAdapter.setList(confirmedRequests);



                        dialog.cancel();
                        BottomNavigationView navigation
                                = (BottomNavigationView) getActivity().findViewById(R.id.vendor_bottom_nav);
                        navigation.setSelectedItemId(R.id.menuItem_vendor_displayCompletedRequests);
                    }
                });

        confirmBuilder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        confirmBuilder.show().getWindow().setLayout(650, 500);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        btn_markCompleted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentRequestPosition != -1){
//                    currentServiceRequest
//                            = (ServiceRequest) confirmedRequests.get(currentRequestPosition);
//
//                    ServiceBid winningBid = currentServiceRequest.getWinningBid();
//
//                    Double serviceCost = winningBid.getAmt();
//
//                    vendorUc = db.getAccount(userId);
//                    vendorWallet = vendorUc.getWalletAmt();
//                    customerUc = db.getAccount(currentServiceRequest.getCustomerId());
//                    customerWallet = customerUc.getWalletAmt();
//                    adminUc = db.getAccount("admin");
//                    adminWallet = adminUc.getWalletAmt();
//
//                    //Calculate new values
//                    customerWallet = customerWallet - serviceCost;
//                    Double vendorPay = 0.8 * serviceCost;
//                    vendorWallet = (Double) (vendorWallet + vendorPay);
//                    adminWallet = (Double) (adminWallet + (0.2 * serviceCost));
//
//                    String msg = "C: " + Double.toString(customerWallet)
//                            + ", V:" + Double.toString(vendorWallet)
//                            + ", A:" + Double.toString(adminWallet);
//                    Log.d("Wallet figures: ", msg);
//
//                    AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getContext());
//                    String dialogMsg = "Bill Customer $" + Double.toString(serviceCost) + "?"
//                            + " You will receive $"
//                            + Double.toString(round(vendorPay, 2));
//                    confirmBuilder.setMessage(dialogMsg);
//                    confirmBuilder.setCancelable(true);
//                    confirmBuilder.setPositiveButton(
//                            "Yes",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // mark as complete
//                                    db.vendor_markServiceRequestAsComplete(currentServiceRequest.getServiceId());
//
//                                    // update wallets
//                                    db.updateWalletBalance(customerUc.getUserId(), round(customerWallet, 2));
//                                    db.updateWalletBalance(vendorUc.getUserId(), round(vendorWallet, 2));
//                                    db.updateWalletBalance(adminUc.getUserId(), round(adminWallet, 2));
//
//                                    // award customer 100 points
//                                    db.setCustomerPoints(customerUc.getUserId(), 100);
//
//                                    // request query for pending service requests
//                                    confirmedRequests = db.getConfirmedRequestsForVendor(userId);
//                                    mListDataAdapter.setList(confirmedRequests);
//
//                                    dialog.cancel();
//                                }
//                            });
//
//                    confirmBuilder.setNegativeButton(
//                            "No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alert11 = confirmBuilder.create();
//                    alert11.show();
//                }
//            }
//        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
