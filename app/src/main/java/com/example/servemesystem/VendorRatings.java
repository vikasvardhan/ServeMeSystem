package com.example.servemesystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorRatings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorRatings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseAccess db;
    List<VendorRating> vendorReviews = new ArrayList<VendorRating>();
    SharedPreferences sharedpreferences;
    ListVendRating mListDataAdapter;
    ListView lvItems;
    int userId;

    public VendorRatings() {
        // Required empty public constructor
    }

    public static VendorRatings newInstance(String param1, String param2) {
        VendorRatings fragment = new VendorRatings();
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
        View view = inflater.inflate(R.layout.fragment_vendor_ratings, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Reviews");

        lvItems = (ListView) view.findViewById (R.id.listView_vendor_ratings);
        sharedpreferences = getActivity().getSharedPreferences(FirstFragment.PREFERENCES,
                                                               Context.MODE_PRIVATE);
        userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        db = DatabaseAccess.getInstance(getActivity());

        userId = mParam1 != null && mParam1.length() > 0 ? Integer.parseInt(mParam1): userId;
        if(userId != -1){
            vendorReviews = db.getRatingsForVendor(userId);
            mListDataAdapter = new ListVendRating(getContext(),
                    R.layout.row_vendor_rating,
                    vendorReviews);
            lvItems.setAdapter(mListDataAdapter);
        }
        return view;
    }
}
