package com.example.servemesystem;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
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

public class ListCustServiceBids extends ArrayAdapter {

    List <ServiceBid> mList;
    CustomerManageBids mParent;

    public ListCustServiceBids(Context context,
                               int resource,
                               List<ServiceBid> list,
                               CustomerManageBids inParent){
        super(context, resource);
        mList = list;
        mParent = inParent;
    }

    static class LayoutHandler{
        TextView vendorName, bidAmount, bidHours, bidNotes;
        Button acceptBid, rejectBid;
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

    public void setList(List<ServiceBid> inList) {
        mList.clear();
        mList.addAll(inList);
//        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
   }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View mView = convertView;
        final LayoutHandler layoutHandler;
        if(mView==null){
            LayoutInflater layoutInflater
                    = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.row_customer_service_bid,
                                            parent,
                                            false);
            layoutHandler = new LayoutHandler();
            layoutHandler.vendorName = (TextView)mView.findViewById(R.id.customerServiceBid_vendorName);
            layoutHandler.bidAmount = (TextView)mView.findViewById(R.id.customerServiceBid_bidAmt);
            layoutHandler.bidHours =(TextView)mView.findViewById(R.id.customerServiceBid_bidDuration);
            layoutHandler.bidNotes =(TextView)mView.findViewById(R.id.customerServiceBid_bidNotes);
            layoutHandler.acceptBid = (Button)mView.findViewById(R.id.customerServiceBid_acceptBid);
            layoutHandler.rejectBid = (Button)mView.findViewById(R.id.customerServiceBid_rejectBid);
            layoutHandler.expandableView
                    = (LinearLayout)mView.findViewById(R.id.customerServiceBid_expandableView);
            layoutHandler.cardView
                    = (MaterialCardView) mView.findViewById(R.id.customerServiceBid_card);
            layoutHandler.expandButton
                    = (ImageView) mView.findViewById(R.id.customerServiceBid_expandBtn);
            mView.setTag(layoutHandler);
        }
        else {
            layoutHandler = (LayoutHandler) mView.getTag();
        }

        ServiceBid serviceBid = (ServiceBid) this.getItem(position);
        layoutHandler.vendorName.setText(serviceBid.getVendorName());
        layoutHandler.bidAmount.setText("$" + Double.toString(serviceBid.getAmt()));
        layoutHandler.bidHours.setText(Integer.toString(serviceBid.getHours()) + " hours");
        layoutHandler.bidNotes.setText(serviceBid.getNotes());
        layoutHandler.acceptBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.acceptBid(position);
            }
        });
        layoutHandler.rejectBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.rejectBid(position);
            }
        });
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
        return mView;
    }
}
