package com.example.servemesystem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import com.google.android.material.card.MaterialCardView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListVendConfirmedServiceRequest extends ArrayAdapter {

    List<ServiceRequest> mList;
    VendorManageConfirmed mParent;

    public ListVendConfirmedServiceRequest(Context context,
                                           int resource,
                                           List<ServiceRequest> list,
                                           VendorManageConfirmed inParent){
        super(context, resource);
        mList = list;
        mParent = inParent;
    }

    static class LayoutHandler{
        TextView category, title, date, cost, duration, location, description, notes, customer;
        ImageView categoryIcon;
        Button markComplete;
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
            mView = layoutInflater.inflate(R.layout.row_vendor_confirmed_request,
                    parent,
                    false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon
                    = (ImageView)mView.findViewById(R.id.vendorConfirmedRequests_categoryIcon);
            layoutHandler.title = (TextView)mView.findViewById(R.id.vendorConfirmedRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.vendorConfirmedRequests_date);
            layoutHandler.cost = (TextView)mView.findViewById(R.id.vendorConfirmedRequests_cost);
            layoutHandler.duration = (TextView)mView.findViewById(R.id.vendorConfirmedRequests_duration);
            layoutHandler.location = (TextView)mView.findViewById(R.id.vendorConfirmedRequests_location);
            layoutHandler.description = (TextView)mView.findViewById(R.id.vendorConfirmedRequests_description);
            layoutHandler.notes = (TextView)mView.findViewById(R.id.vendorConfirmedRequests_notes);
            layoutHandler.customer = (TextView)mView.findViewById(R.id.vendorConfirmedRequests_customer);
            layoutHandler.markComplete
                    = (Button)mView.findViewById(R.id.vendorConfirmedRequests_markComplete);
            layoutHandler.expandableView
                    = (LinearLayout)mView.findViewById(R.id.vendorConfirmedRequests_expandable_view);
            layoutHandler.cardView
                    = (MaterialCardView) mView.findViewById(R.id.vendorConfirmedRequests_card);
            layoutHandler.expandButton
                    = (ImageView) mView.findViewById(R.id.vendorConfirmedRequests_expandBtn);

            mView.setTag(layoutHandler);
        }
        else {
            layoutHandler = (ListVendConfirmedServiceRequest.LayoutHandler) mView.getTag();
        }

        ServiceRequest serviceRequest = (ServiceRequest) this.getItem(position);
        layoutHandler.categoryIcon.setImageResource
                (serviceRequest.getCategory().getCategoryImgId());
        layoutHandler.title.setText(serviceRequest.getTitle());
        layoutHandler.date.setText(serviceRequest.getServiceTime());
        layoutHandler.cost.setText("$" + Double.toString(serviceRequest.getWinningBid().getAmt()));
        layoutHandler.duration.setText(Integer.toString(serviceRequest.getWinningBid().getHours()) + " hours");
        layoutHandler.location.setText(serviceRequest.getLocation());
        layoutHandler.description.setText(serviceRequest.getDescription());
        layoutHandler.notes.setText(serviceRequest.getWinningBid().getNotes());
        layoutHandler.customer.setText(serviceRequest.getRequestedBy());
        layoutHandler.markComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.markComplete(position);
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

        String serviceDate = serviceRequest.getServiceTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        try{
            Date recordDt = df.parse(serviceDate);
            Date currDt = new Date();

            if(currDt.after(recordDt)){
                layoutHandler.markComplete.setVisibility(View.VISIBLE);
            }
            else{
                layoutHandler.markComplete.setVisibility(View.GONE);
            }
        }catch(Exception ex){
            Log.d("DBG: ", "Cannot parse service datetime");
        }
        return mView;
    }

    public void setList(List<ServiceRequest> inList) {
        mList.clear();
        mList.addAll(inList);
//        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
    }
}
