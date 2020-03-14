package com.example.servemesystem;

import android.content.Context;
import android.icu.text.RelativeDateTimeFormatter;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ListVendPendingServiceRequest extends ArrayAdapter {

    List<ServiceRequest> mList;
    VendorManagePending mParent;

    public ListVendPendingServiceRequest(Context context,
                                         int resource,
                                         List<ServiceRequest> list,
                                         VendorManagePending inParent){
        super(context, resource);
        mList = list;
        mParent = inParent;
    }

    static class LayoutHandler{
        TextView title, date, cost, duration, description, notes, location;
        ImageView categoryIcon;
        Button cancelBtn;
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
            mView = layoutInflater.inflate(R.layout.row_vendor_pending_request,
                                           parent,
                                           false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon
                    = (ImageView)mView.findViewById(R.id.vendorPendingRequests_categoryIcon);
            layoutHandler.title = (TextView)mView.findViewById(R.id.vendorPendingRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.vendorPendingRequests_date);
            layoutHandler.cost = (TextView)mView.findViewById(R.id.vendorPendingRequests_cost);
            layoutHandler.duration = (TextView)mView.findViewById(R.id.vendorPendingRequests_duration);
            layoutHandler.description
                    = (TextView)mView.findViewById(R.id.vendorPendingRequests_description);
            layoutHandler.notes = (TextView)mView.findViewById(R.id.vendorPendingRequests_notes);
            layoutHandler.location
                    = (TextView)mView.findViewById(R.id.vendorPendingRequests_location);
            layoutHandler.cancelBtn = (Button)mView.findViewById(R.id.vendorPendingRequests_cancelBtn);
            layoutHandler.expandableView
                    = (LinearLayout)mView.findViewById(R.id.expandable_view);
            layoutHandler.cardView
                    = (MaterialCardView) mView.findViewById(R.id.vendorPendingRequests_card);
            layoutHandler.expandButton
                    = (ImageView) mView.findViewById(R.id.vendorPendingRequests_expandBtn);
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
        layoutHandler.cost.setText("$" + Double.toString(serviceRequest.getPendingBid().getAmt()));
        layoutHandler.duration.setText(Integer.toString(serviceRequest.getPendingBid().getHours()) + " hours");
        layoutHandler.description.setText(serviceRequest.getDescription());
        layoutHandler.notes.setText(serviceRequest.getPendingBid().getNotes());
        layoutHandler.location.setText(serviceRequest.getLocation());
        layoutHandler.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.cancelBid(position);
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
//                    TransitionManager.beginDelayedTransition(layoutHandler.cardView,
//                            new AutoTransition());
                    layoutHandler.expandableView.setVisibility(View.GONE);
                    layoutHandler.expandButton.setImageResource(R.drawable.ic_arrow_downward_24dp);
                }
            }
        });
        return mView;
    }

    public void setList(List<ServiceRequest> inList) {
        mList.clear();
        mList.addAll(inList);
        this.notifyDataSetInvalidated();
    }
}
