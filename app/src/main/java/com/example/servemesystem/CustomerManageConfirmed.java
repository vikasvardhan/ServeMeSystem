package com.example.servemesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    int userId;

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Confirmed Requests");

        ListView lvItems = (ListView) view.findViewById(R.id.listView_customer_confirmed_requests);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
            }
        });

        sharedpreferences = getActivity().getSharedPreferences(FirstFragment.PREFERENCES,
                Context.MODE_PRIVATE);

        db = DatabaseAccess.getInstance(getActivity());

        userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        if (userId != -1) {
            confirmedRequests = db.getConfirmedRequestsForCustomer(userId);
            mListDataAdapter = new ListCustConfirmedServiceRequest(getContext(),
                    R.layout.row_customer_confirmed_request,
                    confirmedRequests,
                    mFrame);
            lvItems.setAdapter(mListDataAdapter);
        }
        return view;
    }

    public void cancelServiceRequest(final int position) {
        currConfirmedRequest
                = (ServiceRequest) confirmedRequests.get(position);
        String username
                = sharedpreferences.getString(UserAccount.USERNAME, "");
        if (!username.equals("")) {
            if (!username.equals("")) {
                UserAccount uc = db.getAccount(username);
                int numPoints = uc.getPoints();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(),
                        R.style.ThemeOverlay_MaterialComponents_Dialog);
                final TextView warningMsg = new TextView(getActivity());

                // Specify the alert dialog title
                String titleText = "Warning!";

                // Initialize a new foreground color span instance
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

                // Initialize a new spannable string builder instance
                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

                // Apply the text color span
                ssBuilder.setSpan(
                        foregroundColorSpan,
                        0,
                        titleText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                final TextView dgTitle = new TextView(getActivity());
                dgTitle.setText("Warning");
                dgTitle.setPadding(20, 30, 20, 30);
                dgTitle.setTextSize(20F);
//                dgTitle.setBackgroundColor(Color.CYAN);
                dgTitle.setTextColor(Color.RED);
                LinearLayout linearLayout = new LinearLayout(getActivity());

                LinearLayout.LayoutParams layoutParams
                        = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                layoutParams.gravity = Gravity.CENTER;

                warningMsg.setText("You will lose "
                                    + Integer.toString(numPoints)
                                    + " points. Continue?");
                dialogBuilder.setTitle(ssBuilder);
//                dialogBuilder.setCustomTitle(dgTitle);
                linearLayout.addView(warningMsg);
                linearLayout.setPadding(60, 15, 60, 0);
                dialogBuilder.setView(linearLayout);

                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Some Code
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
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                dialogBuilder.show().getWindow().setLayout(800, 450);
            }
        }
    }

    public void cancelServiceRequest_withoutPenalty(int position) {
        currConfirmedRequest
                = (ServiceRequest) confirmedRequests.get(position);
        db.customer_invalidateBids(currConfirmedRequest.getServiceId());
        db.customer_cancelServiceRequest(currConfirmedRequest.getServiceId());

        // refresh list
        confirmedRequests
                = db.getConfirmedRequestsForCustomer(userId);
        mListDataAdapter.setList(confirmedRequests);
    }
}
