package com.example.servemesystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

    public VendorManageConfirmed() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_vendor_manage_confirmed,
                                    container,
                                    false);
        ListView lvItems = (ListView) view.findViewById (R.id.listView_vendor_confirmed_requests);
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
            confirmedRequests = db.getConfirmedRequestsForVendor(userId);
            mListDataAdapter
                    = new ListVendConfirmedServiceRequest(getContext(),
                                                          R.layout.row_vendor_confirmed_request,
                                                          confirmedRequests);
            lvItems.setAdapter(mListDataAdapter);
        }
        return view;
    }
}
