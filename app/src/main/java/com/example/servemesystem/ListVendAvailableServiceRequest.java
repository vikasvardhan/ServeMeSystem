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

public class ListVendAvailableServiceRequest extends ArrayAdapter {

    List<ServiceRequest> mList;
    VendorViewAvailable mParent;

    public ListVendAvailableServiceRequest(Context context,
                                           int resource,
                                           List<ServiceRequest> list,
                                           VendorViewAvailable inParent){
        super(context, resource);
        mList = list;
        mParent = inParent;
    }

    static class LayoutHandler{
        TextView title, date, location, description, requestor;
        ImageView categoryIcon;
        Button bidBtn;
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
            mView = layoutInflater.inflate(R.layout.row_vendor_available_request,
                                           parent,
                                           false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon
                    = (ImageView)mView.findViewById(R.id.vendorAvailableRequests_categoryIcon);
            layoutHandler.title = (TextView)mView.findViewById(R.id.vendorAvailableRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.vendorAvailableRequests_date);
            layoutHandler.location=(TextView)mView.findViewById(R.id.vendorAvailableRequests_location);
            layoutHandler.description
                    = (TextView)mView.findViewById(R.id.vendorAvailableRequests_description);
            layoutHandler.requestor
                    = (TextView)mView.findViewById(R.id.vendorAvailableRequests_requestor);
            layoutHandler.bidBtn
                    = (Button) mView.findViewById(R.id.vendorAvailableRequests_bidBtn);
            layoutHandler.expandableView
                    = (LinearLayout)mView.findViewById(R.id.vendorAvailableRequests_expandableView);
            layoutHandler.cardView
                    = (MaterialCardView) mView.findViewById(R.id.vendorAvailableRequests_card);
            layoutHandler.expandButton
                    = (ImageView) mView.findViewById(R.id.vendorAvailableRequests_expandBtn);
            mView.setTag(layoutHandler);
        }
        else {
            layoutHandler = (LayoutHandler) mView.getTag();
        }

        ServiceRequest serviceRequest = (ServiceRequest) this.getItem(position);
        layoutHandler.categoryIcon.setImageResource
                (serviceRequest.getCategory().getCategoryImgId());
        layoutHandler.title.setText(serviceRequest.getTitle());
        layoutHandler.date.setText(serviceRequest.getServiceTime());
        layoutHandler.location.setText(serviceRequest.getLocation());
        layoutHandler.requestor.setText(serviceRequest.getRequestedBy());
        layoutHandler.description.setText(serviceRequest.getDescription());
        layoutHandler.bidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.createNewBid(position);
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

    public void setList(List<ServiceRequest> inList) {
        mList.clear();
        mList.addAll(inList);
//        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
    }
}
