package com.example.servemesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ListCustConfirmedServiceRequest extends ArrayAdapter {

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
        TextView location, title, date, vendor;
        ImageView categoryIcon;
        Button cancelRequestBtn;
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
        ListCustConfirmedServiceRequest.LayoutHandler layoutHandler;
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
            layoutHandler.cancelRequestBtn
                    = (Button)mView.findViewById(R.id.customerConfirmedRequests_cancelRequest);
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
        layoutHandler.cancelRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.cancelServiceRequest(position);
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
