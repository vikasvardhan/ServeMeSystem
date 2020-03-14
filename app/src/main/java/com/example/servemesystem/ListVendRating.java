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
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ListVendRating extends ArrayAdapter {

    List<VendorRating> mList;

    public ListVendRating(Context context, int resource, List<VendorRating> list){
        super(context, resource);
        mList = list;
    }

    static class LayoutHandler{
        TextView customer, serviceTitle, comments;
        RatingBar rating;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View mView = convertView;
        final ListVendRating.LayoutHandler layoutHandler;
        if(mView==null){
            LayoutInflater layoutInflater
                    = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.row_vendor_rating,
                    parent,
                    false);
            layoutHandler = new LayoutHandler();
            layoutHandler.customer = (TextView)mView.findViewById(R.id.vendorRating_customerName);
            layoutHandler.serviceTitle =(TextView)mView.findViewById(R.id.vendorRating_serviceTitle);
            layoutHandler.comments = (TextView)mView.findViewById(R.id.vendorRating_comments);
            layoutHandler.serviceTitle
                    = (TextView)mView.findViewById(R.id.vendorRating_serviceTitle);
            layoutHandler.rating = (RatingBar) mView.findViewById(R.id.vendorRating_ratingBar);

            mView.setTag(layoutHandler);
        }
        else {
            layoutHandler = (ListVendRating.LayoutHandler) mView.getTag();
        }

        VendorRating vendorRating = (VendorRating) this.getItem(position);
        layoutHandler.customer.setText(vendorRating.getRequestedBy());
        layoutHandler.serviceTitle.setText(vendorRating.getServiceTitle());
        layoutHandler.comments.setText(vendorRating.getComment());
        layoutHandler.rating.setRating(vendorRating.getRating());
        return mView;
    }

    @Override
    public int getCount(){
        return mList.size();
    }

    @Override
    public Object getItem(int position){
        return mList.get(position);
    }

    public void setList(List<VendorRating> inList) {
        mList.clear();
        mList.addAll(inList);
        this.notifyDataSetInvalidated();
    }
}
