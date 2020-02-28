package com.example.servemesystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    DatabaseAccess db;
    List<ServiceRequest> pendingRequests;
    ListPendingServiceRequest mListDataAdapter;
    SharedPreferences sharedpreferences;

    public CustomerManagePending() {
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

        ListView lvItems = (ListView) view.findViewById (R.id.listView_customer_pending_requests);
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
            pendingRequests = db.getPendingRequestsForCustomer(userId);
            mListDataAdapter = new ListPendingServiceRequest(getContext(),
                    R.layout.row_customer_pending_request,
                    pendingRequests);
            lvItems.setAdapter(mListDataAdapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Find ListView to populate


        // Setup cursor adapter using cursor from last step
//        CursorAdaptorCustPendingRequests todoAdapter
//                = new CursorAdaptorCustPendingRequests(getContext(), cursor);

        // Attach cursor adapter to the ListView
//        lvItems.setAdapter(todoAdapter);
    }
}
