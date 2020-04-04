package com.example.servemesystem;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
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
    private List<UserAccount> vendorListFull;
    private CustomerSearch mFrame;
    ListCustSearch mListDataAdapter;
    private UserAccount currUserAccount;

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
        //For Search
        setHasOptionsMenu(true);

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
        vendorListFull = new ArrayList<>(vendorList);
        ListView lvItems = (ListView) view.findViewById(R.id.listView_vendor_search);
        mListDataAdapter = new ListCustSearch(getContext(),
                R.layout.row_customer_search,
                vendorList,
                mFrame);
        lvItems.setAdapter(mListDataAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    public void viewReviews(int position) {

        currUserAccount
                = (UserAccount) vendorList.get(position);
        Integer userId = currUserAccount.getUserId();
        Fragment vendorRatings = VendorRatings.newInstance(userId.toString(), "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(vendorRatings, "vendor_reviews")
                .addToBackStack("vendor_reviews")
                .replace(R.id.fragment_container_customer_home, vendorRatings)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();

        inflater.inflate(R.menu.menu_options_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menuItem_options_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHint("Search...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mListDataAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem_options_filter:
                onRatingSelect();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onRatingSelect() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(),
                R.style.ThemeOverlay_MaterialComponents_Dialog);
        final RatingBar input = new RatingBar(getActivity());
        LinearLayout linearLayout = new LinearLayout(getActivity());

        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        input.setLayoutParams(layoutParams);
//        input.onEditorAction(EditorInfo.IME_ACTION_DONE);

        linearLayout.addView(input);
        linearLayout.setPadding(20, 30, 20, 0);
        dialogBuilder.setTitle("Rating Filter");
        dialogBuilder.setView(linearLayout);

        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                List<UserAccount> results = new ArrayList<>();
                if (input.getRating() > 0) {
                    for (UserAccount item : vendorListFull) {
                        if (item.getRating() >= input.getRating())
                            results.add(item);
                    }
                    mListDataAdapter.setList(results);
                } else {
                    results.addAll(vendorListFull);
                    mListDataAdapter.setList(results);
                }

                dialog.cancel();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        dialogBuilder.show().getWindow().setLayout(800, 650);
    }

}
