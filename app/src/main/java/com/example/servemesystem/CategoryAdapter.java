package com.example.servemesystem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryItem> itemList;
    private ItemClickListener itemClickListener;

    public CategoryAdapter(List<CategoryItem> itemList) {
        this.itemList = itemList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CategoryItem item = itemList.get(position);

        holder.categoryName.setText(item.getCategoryName());
        holder.categoryIcon.setImageResource(item.getCategoryImgId());

        holder.vBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryName;
        View vBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            vBackground = itemView;
            categoryIcon = itemView.findViewById(R.id.category_icon);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }

    public interface ItemClickListener {
        void onItemClick(CategoryItem item);
    }
}
