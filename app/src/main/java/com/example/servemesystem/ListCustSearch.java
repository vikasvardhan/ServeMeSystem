package com.example.servemesystem;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ListCustSearch extends ArrayAdapter {

    List <UserAccount> mList;
    CustomerSearch mParent;

    public ListCustSearch(Context context,
                          int resource,
                          List<UserAccount> list,
                          CustomerSearch inParent){
        super(context, resource);
        mList = list;
        mParent = inParent;
    }

    static class LayoutHandler{
        TextView title, phone, location, description, email, requestor;
        ImageView categoryIcon;
        Button viewBids;
        Button cancelRequest;
        ImageView expandButton;
        LinearLayout expandableView;
        MaterialCardView cardView;
    }

    @Override
    public int getCount(){
        return mList.size();
    }

    @Override
    public Object getItem(int position){
        return mList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View mView = convertView;
        final LayoutHandler layoutHandler;
        if(mView==null){
            LayoutInflater layoutInflater
                    = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.row_customer_search,
                                           parent,
                                          false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon = (ImageView)mView.findViewById(R.id.customerPendingRequests_categoryIcon);
            layoutHandler.location = (TextView)mView.findViewById(R.id.customerPendingRequests_location);
            layoutHandler.description = (TextView)mView.findViewById(R.id.customerPendingRequests_description);
            layoutHandler.title = (TextView)mView.findViewById(R.id.customerSearch_title);
            layoutHandler.phone =(TextView)mView.findViewById(R.id.customerSearch_phone);
            layoutHandler.email = (TextView)mView.findViewById(R.id.customerSearch_email);
            layoutHandler.requestor = (TextView) mView.findViewById(R.id.customerPendingRequests_requestor);
            layoutHandler.viewBids = (Button)mView.findViewById(R.id.btn_viewBids);
            layoutHandler.expandableView
                    = (LinearLayout)mView.findViewById(R.id.customerPendingRequests_expandableView);
            layoutHandler.cardView
                    = (MaterialCardView) mView.findViewById(R.id.customerPendingRequests_card);
            layoutHandler.expandButton
                    = (ImageView) mView.findViewById(R.id.customerPendingRequests_expandBtn);
            layoutHandler.cancelRequest
                    = (Button) mView.findViewById(R.id.customerPendingRequests_cancelRequest);
            mView.setTag(layoutHandler);
        }
        else {
            layoutHandler = (LayoutHandler) mView.getTag();
        }

//        ServiceRequest serviceRequest = (ServiceRequest) this.getItem(position);
        UserAccount userAccount = (UserAccount) this.getItem(position);
        layoutHandler.categoryIcon.setImageResource
                (R.drawable.appliances);
        layoutHandler.title.setText(userAccount.getLastName() + ", " +  userAccount.getFirstName());
        layoutHandler.location.setText(userAccount.getAddress());
        layoutHandler.phone.setText(userAccount.getPhone());
        layoutHandler.description.setText(userAccount.getPhone());
        layoutHandler.requestor.setText(userAccount.getUsername());
        layoutHandler.email.setText(userAccount.getEmail());


        layoutHandler.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutHandler.expandableView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(layoutHandler.cardView,
                            new AutoTransition());
                    layoutHandler.expandableView.setVisibility(View.VISIBLE);
                    layoutHandler.expandButton.setImageResource(R.drawable.ic_arrow_upward_24dp);
                }
                else{
                    layoutHandler.expandableView.setVisibility(View.GONE);
//                    TransitionManager.beginDelayedTransition(layoutHandler.cardView,
//                            new AutoTransition());
                    layoutHandler.expandButton.setImageResource(R.drawable.ic_arrow_downward_24dp);
                }
            }
        });
        layoutHandler.cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mParent.cancelRequest(position);
            }
        });
        return mView;
    }

    public void setList(List<UserAccount> inList) {
        mList.clear();
        mList.addAll(inList);
//        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
    }
}
