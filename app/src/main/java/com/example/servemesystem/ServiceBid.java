package com.example.servemesystem;

public class ServiceBid {
    private int bidId;
    private double amt;
    private String notes;
    private int hours;
    private String status;

    public String getStatus() {
        return status;
    }

    public double getAmt() {
        return amt;
    }

    public int getBidId() {
        return bidId;
    }

    public int getHours() {
        return hours;
    }

    public String getNotes() {
        return notes;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
