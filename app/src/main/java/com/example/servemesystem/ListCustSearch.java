package com.example.servemesystem;

import android.content.Context;
import android.media.Rating;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ListCustSearch extends ArrayAdapter implements Filterable {

    List<UserAccount> mList;
    List<UserAccount> mListFull;
    CustomerSearch mParent;

    public ListCustSearch(Context context,
                          int resource,
                          List<UserAccount> list,
                          CustomerSearch inParent) {
        super(context, resource);
        mList = list;
        mListFull = new ArrayList<>(mList);
        mParent = inParent;
    }



    static class LayoutHandler {
        public RatingBar ratingBar;
        TextView title, phone, location, email;
        ImageView categoryIcon;
        Button cancelRequest;
        Button profile;
        ImageView expandButton;
        LinearLayout expandableView;
        MaterialCardView cardView;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        final LayoutHandler layoutHandler;
        if (mView == null) {
            LayoutInflater layoutInflater
                    = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = layoutInflater.inflate(R.layout.row_customer_search,
                    parent,
                    false);
            layoutHandler = new LayoutHandler();
            layoutHandler.categoryIcon = (ImageView) mView.findViewById(R.id.customerPendingRequests_categoryIcon);
            layoutHandler.location = (TextView) mView.findViewById(R.id.customerPendingRequests_location);
            layoutHandler.title = (TextView) mView.findViewById(R.id.customerSearch_title);
            layoutHandler.phone = (TextView) mView.findViewById(R.id.customerSearch_phone);
            layoutHandler.email = (TextView) mView.findViewById(R.id.customerSearch_email);
//            layoutHandler.requestor = (TextView) mView.findViewById(R.id.customerPendingRequests_requestor);
            layoutHandler.ratingBar = (RatingBar) mView.findViewById(R.id.customerSearch_ratingBar);
//            layoutHandler.viewBids = (Button)mView.findViewById(R.id.btn_viewBids);
            layoutHandler.expandableView
                    = (LinearLayout) mView.findViewById(R.id.customerPendingRequests_expandableView);
            layoutHandler.cardView
                    = (MaterialCardView) mView.findViewById(R.id.customerPendingRequests_card);
            layoutHandler.expandButton
                    = (ImageView) mView.findViewById(R.id.customerPendingRequests_expandBtn);
            layoutHandler.cancelRequest
                    = (Button) mView.findViewById(R.id.customerPendingRequests_cancelRequest);
            layoutHandler.profile
                    = (Button) mView.findViewById(R.id.customerPendingRequests_profileBtn);
            mView.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) mView.getTag();
        }

//        ServiceRequest serviceRequest = (ServiceRequest) this.getItem(position);
        UserAccount userAccount = (UserAccount) this.getItem(position);
        if (userAccount.getUserId() % 2 == 0) {
            layoutHandler.categoryIcon.setImageResource
                    (R.drawable.ic_employee_clipart);
        } else {
            layoutHandler.categoryIcon.setImageResource
                    (R.drawable.ic_employee_clipart_primary);
        }
        layoutHandler.title.setText(userAccount.getLastName() + ", " + userAccount.getFirstName());
        layoutHandler.location.setText(userAccount.getAddress() != null ? userAccount.getAddress() : "No Address Given.");
        layoutHandler.phone.setText(userAccount.getPhone());
//        layoutHandler.requestor.setText(userAccount.getUsername());
        layoutHandler.email.setText(userAccount.getEmail());
        layoutHandler.ratingBar.setRating((float) userAccount.getRating());


        layoutHandler.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutHandler.expandableView.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(layoutHandler.cardView,
                            new AutoTransition());
                    layoutHandler.expandableView.setVisibility(View.VISIBLE);
                    layoutHandler.expandButton.setImageResource(R.drawable.ic_arrow_upward_24dp);
                } else {
                    layoutHandler.expandableView.setVisibility(View.GONE);
//                    TransitionManager.beginDelayedTransition(layoutHandler.cardView,
//                            new AutoTransition());
                    layoutHandler.expandButton.setImageResource(R.drawable.ic_arrow_downward_24dp);
                }
            }
        });
        layoutHandler.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.viewReviews(position);
            }
        });
        return mView;
    }

    public void setList(List<UserAccount> inList) {
        mList.clear();
        mList.addAll(inList);
//        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
    }

//    public void searchText(String text){
//        for(UserAccount uAcnt: mList){
//            if(uAcnt.)
//        }
//    }


    @NonNull
    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UserAccount> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mListFull);
            } else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(UserAccount item : mListFull){
                    String name = item.getFirstName() + item.getLastName();
                    if(name.toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList.clear();
            mList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
