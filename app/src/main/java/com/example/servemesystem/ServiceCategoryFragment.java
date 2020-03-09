package com.example.servemesystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceCategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<ServiceCategory> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryView;
    private String type;

    public ServiceCategoryFragment() {
        // Required empty public constructor
    }

    public ServiceCategoryFragment(String inType) {
        type = inType;
        // Required empty public constructor
    }

    public static ServiceCategoryFragment newInstance(String param1, String param2) {
        ServiceCategoryFragment fragment = new ServiceCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_service_category, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Select Category");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryView = view.findViewById(R.id.service_category_list);
        categoryList.add(new ServiceCategory("Appliances"));
        categoryList.add(new ServiceCategory("Electrical"));
        categoryList.add(new ServiceCategory("Plumbing"));
        categoryList.add(new ServiceCategory("Home Cleaning"));
        categoryList.add(new ServiceCategory("Tutoring"));
        categoryList.add(new ServiceCategory("Packaging and Moving"));
        categoryList.add(new ServiceCategory("Computer Repair"));
        categoryList.add(new ServiceCategory("Home Repair and Painting"));
        categoryList.add(new ServiceCategory("Pest Control"));

        categoryAdapter = new CategoryAdapter(categoryList);
        categoryAdapter.setItemClickListener(new CategoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ServiceCategory item) {
                Bundle categoryBundle = new Bundle();
                if(type.equals("customer")){
                    categoryBundle.putString("category_name", item.getCategoryName());
                    Fragment serviceDetailFragment = null;
                    serviceDetailFragment = new ServiceDetailFragment();
                    serviceDetailFragment.setArguments(categoryBundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(serviceDetailFragment, "service_detail")
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack("service_detail")
                            .replace(R.id.fragment_container_customer_home, serviceDetailFragment)
                            .commit();
                }
                else{


//                    categoryBundle.putString("category_name", item.getCategoryName());
                    Fragment availableRequests = new VendorViewAvailable(item.getCategoryName());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(availableRequests, "vendor_view_available")
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack("vendor_view_available")
                            .replace(R.id.fragment_container, availableRequests)
                            .commit();
                }
            }
        });
        categoryView.setAdapter(categoryAdapter);
        categoryView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryView.addItemDecoration(new DividerItemDecoration(getContext(),
                                        DividerItemDecoration.VERTICAL));
    }
}
