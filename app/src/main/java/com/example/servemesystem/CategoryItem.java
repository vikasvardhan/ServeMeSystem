package com.example.servemesystem;

public class CategoryItem {
    private String categoryName;

    public CategoryItem() {

    }

    public CategoryItem(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryImgId()
    {
        int categoryImgId = -1;
        if(this.categoryName.equals("Appliances")){
            categoryImgId = R.drawable.appliances;
        }
        if(this.categoryName.equals("Electrical")){
            categoryImgId = R.drawable.electrical;
        }
        if(this.categoryName.equals("Plumbing")){
            categoryImgId = R.drawable.plumbing;
        }
        if(this.categoryName.equals("Home Cleaning")){
            categoryImgId = R.drawable.cleaning;
        }
        if(this.categoryName.equals("Tutoring")){
            categoryImgId = R.drawable.tutoring;
        }
        if(this.categoryName.equals("Packaging and Moving")){
            categoryImgId = R.drawable.moving;
        }
        if(this.categoryName.equals("Computer Repair")){
            categoryImgId = R.drawable.computer_repair;
        }
        if(this.categoryName.equals("Home Repair and Painting")){
            categoryImgId = R.drawable.painting;
        }
        if(this.categoryName.equals("Pest Control")){
            categoryImgId = R.drawable.pest_control;
        }
        return categoryImgId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
