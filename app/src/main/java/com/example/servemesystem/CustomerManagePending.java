package com.example.servemesystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomerManagePending extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CustomerManagePending mFrame;

    DatabaseAccess db;
    List<ServiceRequest> pendingRequests;
    ListCustPendingServiceRequest mListDataAdapter;
    SharedPreferences sharedpreferences;

    Button viewBids;
//    Button editRequest;

    int currentItemPosition = -1;

    public CustomerManagePending() {
        mFrame = this;
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CustomerManagePending newInstance(String param1, String param2) {
        CustomerManagePending fragment = new CustomerManagePending();
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
        View view = inflater.inflate(R.layout.fragment_customer_manage_pending,
                container,
                false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pending Requests");

        ListView lvItems = (ListView) view.findViewById(R.id.listView_customer_pending_requests);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                currentItemPosition = position;
                if (currentItemPosition != -1) {
//                    viewBids.setEnabled(true);
                    Toast.makeText(getActivity(),
                            String.valueOf(position),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

//        viewBids = (Button) view.findViewById(R.id.btn_viewBids);
//        editRequest = (Button) view.findViewById(R.id.btn_editRequest);

        sharedpreferences = getActivity().getSharedPreferences(FirstFragment.PREFERENCES,
                Context.MODE_PRIVATE);

        db = DatabaseAccess.getInstance(getActivity());

        int userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        if (userId != -1) {
            pendingRequests = db.getPendingRequestsForCustomer(userId);
            mListDataAdapter = new ListCustPendingServiceRequest(getContext(),
                    R.layout.row_customer_pending_request,
                    pendingRequests,
                    mFrame);
            lvItems.setAdapter(mListDataAdapter);
        }
        return view;
    }

    public void viewBids(int position) {
        ServiceRequest currentPendingRequest
                = (ServiceRequest) pendingRequests.get(position);
        Fragment managebidsFragment = new CustomerManageBids(currentPendingRequest);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(managebidsFragment, "manage_bids_fragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("manage_bids_fragment")
                .replace(R.id.fragment_container_customer_manager_requests,
                        managebidsFragment).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        editRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
        // Find ListView to populate

        // Setup cursor adapter using cursor from last step
//        CursorAdaptorCustPendingRequests todoAdapter
//                = new CursorAdaptorCustPendingRequests(getContext(), cursor);

        // Attach cursor adapter to the ListView
//        lvItems.setAdapter(todoAdapter);
    }
}
