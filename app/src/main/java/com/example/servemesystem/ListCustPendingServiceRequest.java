package com.example.servemesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
        TextView location, title, date;
        ImageView categoryIcon;
        Button viewBids;
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
            mView = layoutInflater.inflate(R.layout.row_customer_pending_request,
                                           parent,
                                          false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon = (ImageView)mView.findViewById(R.id.customerPendingRequests_categoryIcon);
            layoutHandler.location = (TextView)mView.findViewById(R.id.customerPendingRequests_location);
            layoutHandler.title = (TextView)mView.findViewById(R.id.customerPendingRequests_title);
            layoutHandler.date =(TextView)mView.findViewById(R.id.customerPendingRequests_date);
            layoutHandler.viewBids = (Button)mView.findViewById(R.id.btn_viewBids);
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

        layoutHandler.viewBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.viewBids(position);
            }
        });
        return mView;
    }
}
