package com.example.servemesystem;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ListCustCompletedServiceRequest extends ArrayAdapter {

    List<ServiceRequest> mList;
    CustomerManageCompleted mParent;

    public ListCustCompletedServiceRequest(Context context,
                                           int resource,
                                           List<ServiceRequest> list,
                                           CustomerManageCompleted inParent){
        super(context, resource);
        mList = list;
        mParent = inParent;
    }

    static class LayoutHandler{
        TextView category, title, date, cost, duration, location, description, notes, vendor;
        ImageView categoryIcon;
        Button reviewBtn;
        ImageView expandButton;
        LinearLayout expandableView;
        MaterialCardView cardView;
        RatingBar ratingBar;
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
            mView = layoutInflater.inflate(R.layout.row_customer_completed_request,
                                           parent,
                                           false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon
                    = (ImageView)mView.findViewById(R.id.customerCompletedRequests_categoryIcon);
            layoutHandler.title = (TextView)mView.findViewById(R.id.customerCompletedRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.customerCompletedRequests_date);
            layoutHandler.location = (TextView)mView.findViewById(R.id.customerCompletedRequests_location);
            layoutHandler.vendor
                    =(TextView)mView.findViewById(R.id.customerCompletedRequests_vendor);
            layoutHandler.cost
                    = (TextView)mView.findViewById(R.id.customerCompletedRequests_cost);
            layoutHandler.duration
                    = (TextView)mView.findViewById(R.id.customerCompletedRequests_duration);
            layoutHandler.description
                    = (TextView)mView.findViewById(R.id.customerCompletedRequests_description);
            layoutHandler.notes
                    = (TextView)mView.findViewById(R.id.customerCompletedRequests_notes);
            layoutHandler.expandableView
                    = (LinearLayout)mView.findViewById(R.id.customerCompletedRequests_expandableView);
            layoutHandler.cardView
                    = (MaterialCardView) mView.findViewById(R.id.customerCompletedRequests_card);
            layoutHandler.expandButton
                    = (ImageView) mView.findViewById(R.id.customerCompletedRequests_expandBtn);
            layoutHandler.ratingBar
                    = (RatingBar) mView.findViewById(R.id.customerCompletedRequests_ratingBar);
            layoutHandler.divider
                    = (View) mView.findViewById(R.id.customerCompletedRequests_hr);

            mView.setTag(layoutHandler);
        }
        else {
            layoutHandler = (ListCustCompletedServiceRequest.LayoutHandler) mView.getTag();
        }

        ServiceRequest serviceRequest = (ServiceRequest) this.getItem(position);
        layoutHandler.title.setText(serviceRequest.getTitle());
        layoutHandler.categoryIcon.setImageResource
                (serviceRequest.getCategory().getCategoryImgId());
        layoutHandler.date.setText(serviceRequest.getServiceTime());
        layoutHandler.location.setText(serviceRequest.getLocation());
        layoutHandler.vendor.setText(serviceRequest.getServicedBy());
        layoutHandler.cost.setText(String.valueOf(serviceRequest.getWinningBid().getAmt()));
        layoutHandler.description.setText(serviceRequest.getDescription());
        layoutHandler.notes.setText(serviceRequest.getWinningBid().getNotes());
        layoutHandler.duration.setText(String.valueOf(serviceRequest.getWinningBid().getHours())
                + " hours");
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

        Log.d("Z89:", serviceRequest.getStatus());

        if(!serviceRequest.getStatus().equals("Cancelled")){
            if(serviceRequest.isReviewed()){
                layoutHandler.ratingBar.setIsIndicator(true);
                layoutHandler.ratingBar.setRating(serviceRequest.getVendorRating().getRating());
            }
            else{
                layoutHandler.ratingBar.setIsIndicator(false);
                layoutHandler.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                        mParent.doRatingBarCallback(position, rating);
                    }
                });
            }
        }
        else{
            layoutHandler.divider.setBackgroundColor(Color.RED);
            layoutHandler.ratingBar.setVisibility(View.GONE);
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
