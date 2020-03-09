package com.example.servemesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView title, date, cost, duration;
        ImageView categoryIcon;
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
    public View getView(int position, View convertView, ViewGroup parent){
        View mView = convertView;
        LayoutHandler layoutHandler;
        if(mView==null){
            LayoutInflater layoutInflater
                    = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.row_vendor_completed_request,
                                           parent,
                                           false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon
                    = (ImageView)mView.findViewById(R.id.vendorCompletedRequests_categoryIcon);
            layoutHandler.title = (TextView)mView.findViewById(R.id.vendorCompletedRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.vendorCompletedRequests_date);
            layoutHandler.cost = (TextView)mView.findViewById(R.id.vendorCompletedRequests_cost);
            layoutHandler.duration = (TextView)mView.findViewById(R.id.vendorCompletedRequests_duration);

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
        return mView;
    }
}
