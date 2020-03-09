package com.example.servemesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListVendAvailableServiceRequest extends ArrayAdapter {

    List<ServiceRequest> mList;
    VendorViewAvailable mParent;

    public ListVendAvailableServiceRequest(Context context,
                                           int resource,
                                           List<ServiceRequest> list,
                                           VendorViewAvailable inParent){
        super(context, resource);
        mList = list;
        mParent = inParent;
    }

    static class LayoutHandler{
        TextView title, date, location, requestedBy;
        ImageView categoryIcon;
        Button bidBtn;
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
        LayoutHandler layoutHandler;
        if(mView==null){
            LayoutInflater layoutInflater
                    = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.row_vendor_available_request,
                                           parent,
                                           false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon
                    = (ImageView)mView.findViewById(R.id.vendorAvailableRequests_categoryIcon);
            layoutHandler.title = (TextView)mView.findViewById(R.id.vendorAvailableRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.vendorAvailableRequests_date);
            layoutHandler.location=(TextView)mView.findViewById(R.id.vendorAvailableRequests_location);
            layoutHandler.requestedBy
                    = (TextView)mView.findViewById(R.id.vendorAvailableRequests_requestedBy);
            layoutHandler.bidBtn
                    = (Button) mView.findViewById(R.id.vendorAvailableRequests_bidBtn);
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
        layoutHandler.location.setText(serviceRequest.getLocation());
        layoutHandler.requestedBy.setText(serviceRequest.getRequestedBy());
        layoutHandler.bidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.createNewBid(position);
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
