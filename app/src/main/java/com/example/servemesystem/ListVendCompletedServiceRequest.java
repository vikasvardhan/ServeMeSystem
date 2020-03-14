package com.example.servemesystem;

import android.content.Context;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ListVendCompletedServiceRequest extends ArrayAdapter {

    List<ServiceRequest> mList;

    public ListVendCompletedServiceRequest(Context context,
                                           int resource,
                                           List<ServiceRequest> list){
        super(context, resource);
        mList = list;
    }

    static class LayoutHandler{
        TextView title, date, cost, duration, customer, description, notes, location;
        ImageView categoryIcon;
        RatingBar ratingBar;
        ImageView expandButton;
        LinearLayout expandableView;
        MaterialCardView cardView;
        View divider;
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
            mView = layoutInflater.inflate(R.layout.row_vendor_completed_request,
                                           parent,
                                           false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon
                    = (ImageView)mView.findViewById(R.id.vendorCompletedRequests_categoryIcon);
            layoutHandler.title
                    = (TextView)mView.findViewById(R.id.vendorCompletedRequests_title);
            layoutHandler.date
                    =(TextView)mView.findViewById(R.id.vendorCompletedRequests_date);
            layoutHandler.cost
                    = (TextView)mView.findViewById(R.id.vendorCompletedRequests_cost);
            layoutHandler.duration
                    = (TextView)mView.findViewById(R.id.vendorCompletedRequests_duration);
            layoutHandler.expandableView
                    = (LinearLayout)mView.findViewById(R.id.vendorCompletedRequests_expandable_view);
            layoutHandler.cardView
                    = (MaterialCardView) mView.findViewById(R.id.vendorCompletedRequests_card);
            layoutHandler.expandButton
                    = (ImageView) mView.findViewById(R.id.vendorCompletedRequests_expandBtn);
            layoutHandler.ratingBar
                    = (RatingBar) mView.findViewById(R.id.vendorCompletedRequests_ratingBar);
            layoutHandler.divider
                    = (View) mView.findViewById(R.id.vendorCompletedRequests_divider);
            layoutHandler.customer
                    = (TextView) mView.findViewById(R.id.vendorCompletedRequests_customer);
            layoutHandler.description
                    = (TextView) mView.findViewById(R.id.vendorCompletedRequests_description);
            layoutHandler.notes
                    = (TextView) mView.findViewById(R.id.vendorCompletedRequests_notes);
            layoutHandler.location
                    = (TextView) mView.findViewById(R.id.vendorCompletedRequests_location);
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
        layoutHandler.cost.setText("$" + Double.toString(serviceRequest.getWinningBid().getAmt()));
        layoutHandler.duration.setText(Integer.toString(serviceRequest.getWinningBid().getHours()) + " hours");
        layoutHandler.description.setText(serviceRequest.getDescription());
        layoutHandler.notes.setText(serviceRequest.getWinningBid().getNotes());
        layoutHandler.location.setText(serviceRequest.getLocation());
        layoutHandler.customer.setText(serviceRequest.getRequestedBy());
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
        if(serviceRequest.isReviewed()
           && serviceRequest.getStatus().equals("Completed")){
            layoutHandler.ratingBar.setVisibility(View.VISIBLE);
            layoutHandler.ratingBar.setRating(serviceRequest.getVendorRating().getRating());
        }
        else{
            layoutHandler.ratingBar.setVisibility(View.INVISIBLE);
        }
        return mView;
    }
}
