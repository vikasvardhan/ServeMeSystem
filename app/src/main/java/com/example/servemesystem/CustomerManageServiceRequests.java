package com.example.servemesystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerManageServiceRequests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerManageServiceRequests extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerManageServiceRequests() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CustomerManageServiceRequests newInstance(String param1, String param2) {
        CustomerManageServiceRequests fragment = new CustomerManageServiceRequests();
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
        View view
                = inflater.inflate(R.layout.fragment_customer_manage_service_requests,
                                   container,
                                   false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView navigation
                = (BottomNavigationView) getActivity().findViewById(R.id.customer_bottom_nav);
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.menuItem_customer_displayPendingRequests:
                        selectedFragment = new CustomerManagePending();
                        break;
                    case R.id.menuItem_customer_displayConfirmedRequests:
                        selectedFragment = new CustomerManageConfirmed();
                        break;
                    case R.id.menuItem_customer_displayCompletedRequests:
                        selectedFragment = new CustomerManageCompleted();
                        break;
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_manager_requests,
                        selectedFragment).commit();
                return true;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_customer_manager_requests, new CustomerManagePending()).commit();
    }
}
