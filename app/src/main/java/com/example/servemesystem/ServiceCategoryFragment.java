package com.example.servemesystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    private List<CategoryItem> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryView;

    public ServiceCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        categoryView = view.findViewById(R.id.service_category_list);
        categoryList.add(new CategoryItem("Appliances"));
        categoryList.add(new CategoryItem("Electrical"));
        categoryList.add(new CategoryItem("Plumbing"));
        categoryList.add(new CategoryItem("Home Cleaning"));
        categoryList.add(new CategoryItem("Tutoring"));
        categoryList.add(new CategoryItem("Packaging and Moving"));
        categoryList.add(new CategoryItem("Computer Repair"));
        categoryList.add(new CategoryItem("Home Repair and Painting"));
        categoryList.add(new CategoryItem("Pest Control"));

        categoryAdapter = new CategoryAdapter(categoryList);
        categoryAdapter.setItemClickListener(new CategoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(CategoryItem item) {
                Bundle categoryBundle = new Bundle();
                categoryBundle.putString("category_name", item.getCategoryName());
                Fragment serviceDetailFragment = null;
                serviceDetailFragment = new ServiceDetailFragment();
                serviceDetailFragment.setArguments(categoryBundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(serviceDetailFragment, "service_detail")
                        .addToBackStack("service_detail")
                        .replace(R.id.fragment_container_customer_home, serviceDetailFragment)
                        .commit();
            }
        });

        categoryView.setAdapter(categoryAdapter);
        categoryView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
}
