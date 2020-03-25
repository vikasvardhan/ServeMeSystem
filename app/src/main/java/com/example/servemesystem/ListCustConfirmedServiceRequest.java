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

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListCustConfirmedServiceRequest extends ArrayAdapter {

    long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
    List<ServiceRequest> mList;
    CustomerManageConfirmed mParent;

    public ListCustConfirmedServiceRequest(Context context,
                                           int resource,
                                           List<ServiceRequest> list,
                                           CustomerManageConfirmed inParent){
        super(context, resource);
        mList = list;
        mParent = inParent;
    }

    static class LayoutHandler{
        TextView location, title, date, vendor, cost, description, notes, duration;
        ImageView categoryIcon;
        Button cancelRequestBtn;
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
            mView = layoutInflater.inflate(R.layout.row_customer_confirmed_request,
                                           parent,
                                           false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon = (ImageView)mView.findViewById(R.id.customerConfirmedRequests_categoryIcon);
            layoutHandler.title = (TextView)mView.findViewById(R.id.customerConfirmedRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.customerConfirmedRequests_date);
            layoutHandler.location = (TextView)mView.findViewById(R.id.customerConfirmedRequests_location);
            layoutHandler.vendor =(TextView)mView.findViewById(R.id.customerConfirmedRequests_vendor);
            layoutHandler.cost = (TextView)mView.findViewById(R.id.customerConfirmedRequests_cost);
            layoutHandler.description
                    = (TextView)mView.findViewById(R.id.customerConfirmedRequests_description);
            layoutHandler.notes
                    = (TextView)mView.findViewById(R.id.customerConfirmedRequests_notes);
            layoutHandler.duration
                    = (TextView)mView.findViewById(R.id.customerConfirmedRequests_duration);
            layoutHandler.cancelRequestBtn
                    = (Button)mView.findViewById(R.id.customerConfirmedRequests_cancelRequest);
            layoutHandler.expandableView
                    = (LinearLayout)mView.findViewById(R.id.customerConfirmedRequests_expandable_view);
            layoutHandler.cardView
                    = (MaterialCardView) mView.findViewById(R.id.customerConfirmedRequests_card);
            layoutHandler.expandButton
                    = (ImageView) mView.findViewById(R.id.customerConfirmedRequests_expandBtn);
            mView.setTag(layoutHandler);
        }
        else {
            layoutHandler = (ListCustConfirmedServiceRequest.LayoutHandler) mView.getTag();
        }

        ServiceRequest serviceRequest = (ServiceRequest) this.getItem(position);
        layoutHandler.categoryIcon.setImageResource
                (serviceRequest.getCategory().getCategoryImgId());
        layoutHandler.title.setText(serviceRequest.getTitle());
        layoutHandler.date.setText(serviceRequest.getServiceTime());
        layoutHandler.location.setText(serviceRequest.getLocation());
        layoutHandler.vendor.setText(serviceRequest.getServicedBy());
        layoutHandler.cost.setText(String.valueOf(serviceRequest.getWinningBid().getAmt()));
        layoutHandler.description.setText(serviceRequest.getDescription());
        layoutHandler.notes.setText(serviceRequest.getWinningBid().getNotes());
        layoutHandler.duration.setText(String.valueOf(serviceRequest.getWinningBid().getHours())
                                        + " hours");

        String serviceDate = serviceRequest.getServiceTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        try{
            Date recordDt = df.parse(serviceDate);
            Date currDt = new Date();

            boolean lessThanDay = Math.abs(currDt.getTime() - recordDt.getTime()) < MILLIS_PER_DAY;
            if(lessThanDay){
                layoutHandler.cancelRequestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mParent.cancelServiceRequest(position);
                    }
                });
            } else{
                layoutHandler.cancelRequestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mParent.cancelServiceRequest_withoutPenalty(position);
                    }
                });
            }
        }catch(Exception ex){
            Log.d("DBG: ", "Cannot parse service datetime");
        }

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
//        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
    }
}
