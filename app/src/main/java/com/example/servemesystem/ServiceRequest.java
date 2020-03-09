package com.example.servemesystem;

import java.sql.Date;

public class ServiceRequest {

    private int serviceId;
    private int customerId;
    private int vendorId;
    private ServiceCategory category;
    private String serviceTime;
    private String location;
    private String title;
    private String description;
    private String status;
    private boolean isReviewed;
    private String servicedBy;
    private String requestedBy;
    private ServiceBid winningBid;
    private ServiceBid pendingBid;

    public ServiceRequest()
    {

    }

    public void setCustomerId(int customerId){
        this.customerId = customerId;
    }

    public void setVendorId(int vendorId){
        this.vendorId = vendorId;
    }

    public void setCategory(ServiceCategory category){
        this.category = category;
    }

    public void setServiceTime(String serviceTime){
        this.serviceTime = serviceTime;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setServicedBy(String servicedBy) {
        this.servicedBy = servicedBy;
    }

    public void setWinningBid(ServiceBid winningBid) {
        this.winningBid = winningBid;
    }

    public void setPendingBid(ServiceBid pendingBid) {
        this.pendingBid = pendingBid;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public ServiceBid getWinningBid() {
        return winningBid;
    }

    public ServiceBid getPendingBid() {
        return pendingBid;
    }

    public int getCustomerId(){
        return customerId;
    }

    public int getVendorId(){
        return vendorId;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getServicedBy() {
        return servicedBy;
    }

    public String getRequestedBy() {
        return requestedBy;
    }
}
