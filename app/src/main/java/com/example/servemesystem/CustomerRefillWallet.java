package com.example.servemesystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerRefillWallet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerRefillWallet extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences sharedpreferences;
    DatabaseAccess db;
    int userId;

    private double currBalance;
    private double refillAmt;

    public CustomerRefillWallet() {
        // Required empty public constructor
    }

    public CustomerRefillWallet(double inCurrBalance, double inRefillAmt){
        currBalance = inCurrBalance;
        refillAmt = inRefillAmt;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerRefillWallet.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerRefillWallet newInstance(String param1, String param2) {
        CustomerRefillWallet fragment = new CustomerRefillWallet();
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
        View view = inflater.inflate(R.layout.fragment_customer_refill_wallet, container, false);

        sharedpreferences
                = getActivity().getSharedPreferences(FirstFragment.PREFERENCES, Context.MODE_PRIVATE);

        db = DatabaseAccess.getInstance(getActivity());
        userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        UserAccount uc = db.getAccount(userId);

        CardForm cardForm = (CardForm) view.findViewById(R.id.card_form);
        TextView txtDes = (TextView) view.findViewById(R.id.payment_amount);
        Button btnPay = (Button) view.findViewById(R.id.btn_pay);

        txtDes.setText("$" + refillAmt);
        btnPay.setText("Refill Wallet");

        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                db.updateWalletBalance(userId, (currBalance + refillAmt));
                Fragment custProfile = new CustomerProfile();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(custProfile, "customer_profile")
                        .addToBackStack("customer_profile")
                        .replace(R.id.fragment_container_customer_home, custProfile)
                        .commit();
            }
        });
        return view;
    }
}
