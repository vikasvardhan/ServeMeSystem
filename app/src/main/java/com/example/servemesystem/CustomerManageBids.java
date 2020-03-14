package com.example.servemesystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerManageBids#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerManageBids extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ServiceRequest mServiceRequest;
    int currentBidPosition;
    Button btn_acceptBid;
    Button btn_rejectBid;

    DatabaseAccess db;
    List<ServiceBid> serviceBids = new ArrayList<ServiceBid>();
//    ArrayList<myList> myArrayList=new ArrayList<myList>();
    ListCustServiceBids mListDataAdapter;
    ListView lvItems;
    CustomerManageBids mFrame;

    public CustomerManageBids() {
        mFrame = this;
    }

    public CustomerManageBids(ServiceRequest serviceRequest) {
        mServiceRequest = serviceRequest;
        mFrame = this;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerManageBids.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerManageBids newInstance(String param1, String param2) {
        CustomerManageBids fragment = new CustomerManageBids();
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

//        lvItems = (ListView) getActivity().findViewById((R.id.listView_customer_manage_bids));
//        TextView emptyText = (TextView) getActivity().findViewById(android.R.id.empty);
//        lvItems.setEmptyView(emptyText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view
                = inflater.inflate(R.layout.fragment_customer_manage_bids, container, false);

        lvItems = (ListView) view.findViewById (R.id.listView_customer_manage_bids);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                view.setSelected(true);
                currentBidPosition = position;
                if(currentBidPosition != -1){
                    btn_acceptBid.setEnabled(true);
                    btn_rejectBid.setEnabled(true);
                }
            }
        });

//        btn_acceptBid = (Button) view.findViewById(R.id.btn_acceptBid);
//        btn_rejectBid = (Button) view.findViewById(R.id.btn_rejectBid);

        db = DatabaseAccess.getInstance(getActivity());

        TextView text
                = (TextView) view.findViewById(R.id.textView_customerManageBids_serviceTitle);
        text.setText(mServiceRequest.getTitle());

        serviceBids = db.getBidsForService(mServiceRequest.getServiceId());
        mListDataAdapter = new ListCustServiceBids(getContext(),
                                                   R.layout.row_customer_service_bid,
                                                   serviceBids,
                                                   mFrame);
        lvItems.setAdapter(mListDataAdapter);
        return view;
    }

    public void rejectBid(int position) {
        ServiceBid currentServiceBid
                = (ServiceBid) serviceBids.get(position);
        int bidId = currentServiceBid.getBidId();
        db.rejectBid(bidId);

        //query for pending service bids again
        serviceBids = db.getBidsForService(mServiceRequest.getServiceId());
        mListDataAdapter.setList(serviceBids);
        String toastMsg = "Rejected bid from: " + currentServiceBid.getVendorName();
        Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
    }

    public void acceptBid(int position) {
        ServiceBid currentServiceBid
                = (ServiceBid) serviceBids.get(position);

        //accept bid
        int bidId = currentServiceBid.getBidId();
        db.acceptBid(bidId);

        //set vendor
        int winningBidderId = currentServiceBid.getVendorId();
        db.setVendorToSeviceRequest(winningBidderId, mServiceRequest.getServiceId());

        //query for pending service bids again
//                    serviceBids = db.getBidsForService(mServiceRequest.getServiceId());

        //refresh list
//                    mListDataAdapter.setList(serviceBids);

        //display notification
        String toastMsg = "Bid awarded to: " + currentServiceBid.getVendorName();
        Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();

        //redirect to confirmed
        Fragment manageConfirmed = new CustomerManageConfirmed();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_manager_requests,
                manageConfirmed).commit();
//                    Toast.makeText(getActivity(), String.valueOf(bidId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        btn_rejectBid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentBidPosition != -1){
//                    ServiceBid currentServiceBid
//                            = (ServiceBid) serviceBids.get(currentBidPosition);
//                    int bidId = currentServiceBid.getBidId();
//                    db.rejectBid(bidId);
//
//                    //query for pending service bids again
//                    serviceBids = db.getBidsForService(mServiceRequest.getServiceId());
//                    mListDataAdapter.setList(serviceBids);
//                    String toastMsg = "Rejected bid from: " + currentServiceBid.getVendorName();
//                    Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        btn_acceptBid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentBidPosition != -1){
//                    ServiceBid currentServiceBid
//                            = (ServiceBid) serviceBids.get(currentBidPosition);
//
//                    //accept bid
//                    int bidId = currentServiceBid.getBidId();
//                    db.acceptBid(bidId);
//
//                    //set vendor
//                    int winningBidderId = currentServiceBid.getVendorId();
//                    db.setVendorToSeviceRequest(winningBidderId, mServiceRequest.getServiceId());
//
//                    //query for pending service bids again
////                    serviceBids = db.getBidsForService(mServiceRequest.getServiceId());
//
//                    //refresh list
////                    mListDataAdapter.setList(serviceBids);
//
//                    //display notification
//                    String toastMsg = "Bid awarded to: " + currentServiceBid.getVendorName();
//                    Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
//
//                    //redirect to confirmed
//                    Fragment manageConfirmed = new CustomerManageConfirmed();
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_manager_requests,
//                            manageConfirmed).commit();
////                    Toast.makeText(getActivity(), String.valueOf(bidId), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
    }
}
