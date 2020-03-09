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
        TextView category, title, date, cost, duration;
        ImageView categoryIcon;
        Button markComplete;
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
        ListVendConfirmedServiceRequest.LayoutHandler layoutHandler;
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
            layoutHandler.markComplete
                    = (Button)mView.findViewById(R.id.vendorConfirmedRequests_markComplete);

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
        layoutHandler.markComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.markComplete(position);
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
