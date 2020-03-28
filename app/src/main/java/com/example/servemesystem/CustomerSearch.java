package com.example.servemesystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerSearch extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseAccess db;
    private List<UserAccount> vendorList;
    private CustomerSearch mFrame;
    ListCustSearch mListDataAdapter;

    public CustomerSearch() {
        mFrame = this;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerSearch newInstance(String param1, String param2) {
        CustomerSearch fragment = new CustomerSearch();
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
        View view = inflater.inflate(R.layout.fragment_customer_search,
                container,
                false);
        db = DatabaseAccess.getInstance(getActivity());
        vendorList = db.getVendorsForSearch();
        ListView lvItems = (ListView) view.findViewById(R.id.listView_vendor_search);
        mListDataAdapter = new ListCustSearch(getContext(),
                R.layout.row_customer_search,
                vendorList,
                mFrame);
        lvItems.setAdapter(mListDataAdapter);
        // Inflate the layout for this fragment
        return view;
    }
}
