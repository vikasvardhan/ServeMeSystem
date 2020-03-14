package com.example.servemesystem;

public class VendorRating {

    private String comment;
    private String reviewedBy;
    private String serviceTitle;
    private float rating;

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getRequestedBy() {
        return reviewedBy;
    }

    public void setRequestedBy(String inReviewedBy) {
        this.reviewedBy = inReviewedBy;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
