package com.example.servemesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListCustCompletedServiceRequest extends ArrayAdapter {

    List<ServiceRequest> mList;

    public ListCustCompletedServiceRequest(Context context,
                                           int resource,
                                           List<ServiceRequest> list){
        super(context, resource);
        mList = list;
    }

    static class LayoutHandler{
        TextView location, title, date, vendor;
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
        ListCustCompletedServiceRequest.LayoutHandler layoutHandler;
        if(mView==null){
            LayoutInflater layoutInflater
                    = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.row_customer_completed_request,
                                           parent,
                                           false);
            layoutHandler = new LayoutHandler();
//            layoutHandler.category = (TextView)mView.findViewById(R.id.customerCompletedRequests_category);
            layoutHandler.categoryIcon
                    = (ImageView)mView.findViewById(R.id.customerCompletedRequests_categoryIcon);
            layoutHandler.title
                    = (TextView)mView.findViewById(R.id.customerCompletedRequests_title);
            layoutHandler.location
                    = (TextView)mView.findViewById(R.id.customerCompletedRequests_location);
            layoutHandler.date
                    = (TextView)mView.findViewById(R.id.customerCompletedRequests_date);
            layoutHandler.vendor
                    =(TextView)mView.findViewById(R.id.customerCompletedRequests_vendor);

            mView.setTag(layoutHandler);
        }
        else {
            layoutHandler = (ListCustCompletedServiceRequest.LayoutHandler) mView.getTag();
        }

        ServiceRequest serviceRequest = (ServiceRequest) this.getItem(position);
//        layoutHandler.category.setText(serviceRequest.getCategory().getCategoryName());
        layoutHandler.title.setText(serviceRequest.getTitle());
        layoutHandler.categoryIcon.setImageResource
                (serviceRequest.getCategory().getCategoryImgId());
        layoutHandler.location.setText(serviceRequest.getLocation());
        layoutHandler.date.setText(serviceRequest.getServiceTime());
        layoutHandler.vendor.setText(serviceRequest.getServicedBy());
        return mView;
    }
}
