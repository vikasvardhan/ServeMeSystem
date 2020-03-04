package com.example.servemesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListCustServiceBids extends ArrayAdapter {

    List <ServiceBid> mList;

    public ListCustServiceBids(Context context,
                               int resource,
                               List<ServiceBid> list){
        super(context, resource);
        mList = list;
    }

    static class LayoutHandler{
        TextView vendorName, bidAmount, bidHours, bidNotes;
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
    public View getView(int position, View convertView, ViewGroup parent){
        View mView = convertView;
        LayoutHandler layoutHandler;
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
        return mView;
    }
}
