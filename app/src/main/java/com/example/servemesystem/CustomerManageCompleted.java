package com.example.servemesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class CustomerManageCompleted extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseAccess db;
    List<ServiceRequest> completedRequests;
    ListCustCompletedServiceRequest mListDataAdapter;
    SharedPreferences sharedpreferences;
    CustomerManageCompleted mFrame;
    int userId;

    public CustomerManageCompleted() {
        // Required empty public constructor
        mFrame = this;
    }

    public static CustomerManageCompleted newInstance(String param1, String param2) {
        CustomerManageCompleted fragment = new CustomerManageCompleted();
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
        View view = inflater.inflate(R.layout.fragment_customer_manage_completed,
                                    container,
                                    false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Completed Requests");
        ListView lvItems = (ListView) view.findViewById (R.id.listView_customer_completed_requests);
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
        if(userId != -1){
            completedRequests = db.getCompletedRequestsForCustomer(userId);
            mListDataAdapter = new ListCustCompletedServiceRequest(getContext(),
                                                                   R.layout.row_customer_completed_request,
                                                                   completedRequests,
                                                                   mFrame);
            lvItems.setAdapter(mListDataAdapter);
        }
        return view;
    }

    public void doRatingBarCallback(final int position, final float rating){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(),
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        final EditText input = new EditText(getActivity());
        LinearLayout linearLayout = new LinearLayout(getActivity());

        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setSingleLine(false);  //add this
        input.setLines(4);
        input.setMaxLines(5);
        input.setGravity(Gravity.LEFT | Gravity.TOP);
        input.setWidth(200);
        input.setHint("(Optional)");
        input.setLayoutParams(layoutParams);
//        input.onEditorAction(EditorInfo.IME_ACTION_DONE);

        linearLayout.addView(input);
        linearLayout.setPadding(60, 0, 60, 0);
        dialogBuilder.setTitle("Comment");
        dialogBuilder.setView(linearLayout);

        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Some Code
                String comment = input.getText().toString();
                ServiceRequest currCompletedRequest
                        = (ServiceRequest) completedRequests.get(position);
//                Toast.makeText(getActivity(),
//                        "Service ID" + String.valueOf(currCompletedRequest.getServiceId()) + ", Comment: " + comment,
//                        Toast.LENGTH_SHORT).show();
                boolean rtn = db.insertVendorRating(currCompletedRequest.getVendorId(),
                                                   userId,
                                                   currCompletedRequest.getServiceId(),
                                                   rating,
                                                   comment);
                db.setServiceRequestAsReviewed(currCompletedRequest.getServiceId());
                db.updateVendorRating(currCompletedRequest.getVendorId());

                dialog.cancel();

                // Refresh list
                completedRequests = db.getCompletedRequestsForCustomer(userId);
                mListDataAdapter.setList(completedRequests);
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        dialogBuilder.show().getWindow().setLayout(800,650);
    }
}
