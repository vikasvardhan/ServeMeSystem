package com.example.servemesystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class VendorManagePending extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseAccess db;
    List<ServiceRequest> pendingRequests;
    ListVendPendingServiceRequest mListDataAdapter;
    SharedPreferences sharedpreferences;
    VendorManagePending mFrame;

//    Button btn_cancelBid;
    int userId;
    int currentRequestPosition;

    public VendorManagePending() {
        // Required empty public constructor
        mFrame = this;
    }

    public static VendorManagePending newInstance(String param1, String param2) {
        VendorManagePending fragment = new VendorManagePending();
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
        View view = inflater.inflate(R.layout.fragment_vendor_manage_pending,
                container,
                false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Pending Requests");

        ListView lvItems = (ListView) view.findViewById (R.id.listView_vendor_pending_requests);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                currentRequestPosition = position;
                if(currentRequestPosition != -1){
//                    btn_cancelBid.setEnabled(true);
                }
            }
        });

//        btn_cancelBid = (Button) view.findViewById(R.id.btn_vendCancelBid);
        sharedpreferences = getActivity().getSharedPreferences(FirstFragment.PREFERENCES,
                Context.MODE_PRIVATE);

        db = DatabaseAccess.getInstance(getActivity());

        userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        if(userId != -1){
            pendingRequests = db.getPendingRequestsForVendor(userId);
            mListDataAdapter
                    = new ListVendPendingServiceRequest(getContext(),
                    R.layout.row_vendor_pending_request,
                    pendingRequests,
                    mFrame);
            lvItems.setAdapter(mListDataAdapter);
        }
        return view;
    }

    public void cancelBid(int position) {
        ServiceRequest currentServiceRequest
                = (ServiceRequest) pendingRequests.get(position);

        ServiceBid pendingBid = currentServiceRequest.getPendingBid();
        int pendingBidId = pendingBid.getBidId();

        // cancel bid
        db.vendor_cancelBid(pendingBidId);

        // request query for pending service requests
        pendingRequests = db.getPendingRequestsForVendor(userId);
        mListDataAdapter.setList(pendingRequests);
        Toast.makeText(getActivity(), "Cancelled Bid", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        btn_cancelBid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentRequestPosition != -1){
//                    ServiceRequest currentServiceRequest
//                            = (ServiceRequest) pendingRequests.get(currentRequestPosition);
//
//                    ServiceBid pendingBid = currentServiceRequest.getPendingBid();
//                    int pendingBidId = pendingBid.getBidId();
//
//                    // cancel bid
//                    db.vendor_cancelBid(pendingBidId);
//
//                    // request query for pending service requests
//                    pendingRequests = db.getPendingRequestsForVendor(userId);
//                    mListDataAdapter.setList(pendingRequests);
////                    String toastMsg = "Rejected bid from: " + currentServiceBid.getVendorName();
//                    Toast.makeText(getActivity(), "Cancelled Bid", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
