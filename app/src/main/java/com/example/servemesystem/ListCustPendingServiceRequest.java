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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ListCustPendingServiceRequest extends ArrayAdapter {

    List <ServiceRequest> mList;
    CustomerManagePending mParent;

    public ListCustPendingServiceRequest(Context context,
                                         int resource,
                                         List<ServiceRequest> list,
                                         CustomerManagePending inParent){
        super(context, resource);
        mList = list;
        mParent = inParent;
    }

    static class LayoutHandler{
        TextView title, date, location, description, numBids, requestor;
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
            mView = layoutInflater.inflate(R.layout.row_customer_pending_request,
                                           parent,
                                          false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon = (ImageView)mView.findViewById(R.id.customerPendingRequests_categoryIcon);
            layoutHandler.location = (TextView)mView.findViewById(R.id.customerPendingRequests_location);
            layoutHandler.description = (TextView)mView.findViewById(R.id.customerPendingRequests_description);
            layoutHandler.title = (TextView)mView.findViewById(R.id.customerPendingRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.customerPendingRequests_date);
            layoutHandler.numBids = (TextView)mView.findViewById(R.id.customerPendingRequests_numBids);
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

        ServiceRequest serviceRequest = (ServiceRequest) this.getItem(position);
        layoutHandler.categoryIcon.setImageResource
                (serviceRequest.getCategory().getCategoryImgId());
        layoutHandler.title.setText(serviceRequest.getTitle());
        layoutHandler.location.setText(serviceRequest.getLocation());
        layoutHandler.date.setText(serviceRequest.getServiceTime());
        layoutHandler.description.setText(serviceRequest.getDescription());
        layoutHandler.requestor.setText(serviceRequest.getRequestedBy());
        int numBids = serviceRequest.getNumBids();
        layoutHandler.numBids.setText(String.valueOf(numBids) + " bid(s)");
        Log.d("Num Bids", String.valueOf(numBids));
        if(numBids > 0){
            layoutHandler.viewBids.setVisibility(View.VISIBLE);
            layoutHandler.viewBids.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mParent.viewBids(position);
                }
            });
        }
        else{
            layoutHandler.viewBids.setVisibility(View.GONE);
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
                mParent.cancelRequest(position);
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
