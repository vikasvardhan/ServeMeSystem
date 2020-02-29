package com.example.servemesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListVendConfirmedServiceRequest extends ArrayAdapter {

    List<ServiceRequest> mList;

    public ListVendConfirmedServiceRequest(Context context,
                                           int resource,
                                           List<ServiceRequest> list){
        super(context, resource);
        mList = list;
    }

    static class LayoutHandler{
        TextView category, title, date, amt, hours;
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
        ListVendConfirmedServiceRequest.LayoutHandler layoutHandler;
        if(mView==null){
            LayoutInflater layoutInflater
                    = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.row_vendor_confirmed_request,
                                           parent,
                                           false);
            layoutHandler = new LayoutHandler();
            layoutHandler.category = (TextView)mView.findViewById(R.id.vendorConfirmedRequests_category);
            layoutHandler.title = (TextView)mView.findViewById(R.id.vendorConfirmedRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.vendorConfirmedRequests_date);

            mView.setTag(layoutHandler);
        }
        else {
            layoutHandler = (ListVendConfirmedServiceRequest.LayoutHandler) mView.getTag();
        }

        ServiceRequest serviceRequest = (ServiceRequest) this.getItem(position);
        layoutHandler.category.setText(serviceRequest.getCategory());
        layoutHandler.title.setText(serviceRequest.getTitle());
        layoutHandler.date.setText(serviceRequest.getServiceTime());
        return mView;
    }
}
