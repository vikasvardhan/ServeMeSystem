package com.example.servemesystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context) {
        context.deleteDatabase(DatabaseHelper.DB_NAME);
        this.openHelper = new DatabaseHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null)
            this.db.close();
    }

    public List<ServiceBid> getBidsForService(int serviceId){
        List<ServiceBid> serviceBids = new LinkedList<ServiceBid>();
        String query = "SELECT * FROM service_bids" +
                " INNER JOIN user_account on service_bids.Vendor_ID=user_account.user_id" +
                " WHERE Status='Pending' and Service_ID=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(serviceId)});
        if(cursor.moveToFirst()) {
            do {
                ServiceBid bid = new ServiceBid();  //Set Bid
                bid.setBidId(cursor.getInt(cursor.getColumnIndex("Bid_ID")));
                bid.setAmt(cursor.getDouble(cursor.getColumnIndex("Amount")));
                bid.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                bid.setHours(cursor.getInt(cursor.getColumnIndex("Hours")));
                bid.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                bid.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String name = firstName + " " + lastName;
                bid.setVendorName(name);
                serviceBids.add(bid);
            } while (cursor.moveToNext());
        }
        return serviceBids;
    }

    public List<ServiceRequest> getAvailableRequestsForVendor(String category, int userId) {
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT * from service_request"
                + " LEFT JOIN user_account"
                + " ON service_request.User_ID=user_account.user_id"
                + " WHERE service_request.Category=?"
                + " AND Service_ID NOT IN"
                + " (SELECT Service_ID FROM service_bids WHERE Vendor_ID=? and service_bids.Status='Pending')"
                + " AND service_request.Status='Pending'";

        Log.d("SQL Query", query);

        Cursor cursor = db.rawQuery(query, new String[]{category, String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if (cursor.moveToFirst()) {
            do {
                serviceRequest = new ServiceRequest();
                serviceRequest.setServiceId(cursor.getInt(cursor.getColumnIndex("Service_ID")));
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory
                        (new ServiceCategory(cursor.getString(cursor.getColumnIndex("Category"))));
                serviceRequest.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                serviceRequest.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                serviceRequest.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                serviceRequest.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setReviewed(cursor.getInt(cursor.getColumnIndex("Is_Reviewed")) > 0);
                serviceRequest.setServiceTime(cursor.getString(cursor.getColumnIndex("Datetime")));
                String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String name = lastName + ", " + firstName;
                serviceRequest.setRequestedBy(name);
                serviceRequests.add(serviceRequest);
            } while (cursor.moveToNext());
        }
//        for (ServiceRequest sr : serviceRequests) {
//            Log.d("SQL Query Category", sr.getCategory());
//            Log.d("SQL Query Title", sr.getTitle());
//        }
        return serviceRequests;
    }

    public List<ServiceRequest> getPendingRequestsForVendor(int userId){
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT service_request.Service_ID, service_request.User_ID, service_request.Category, service_request.Datetime," +
                " service_request.Location, service_request.Title, service_request.Description, service_request.Status, service_request.Is_Reviewed," +
                " service_bids.Bid_ID, service_bids.Amount, service_bids.Notes, service_bids.Hours, service_bids.Status, service_bids.Vendor_ID" +
                " FROM service_request" +
                " INNER JOIN service_bids" +
                " ON service_request.Service_ID=service_bids.Service_ID" +
                " WHERE service_request.Status='Pending' and service_bids.Status='Pending' and service_bids.Vendor_ID=?" +
                " ORDER BY service_request.Service_ID DESC";

        Log.d("SQL Query", query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if(cursor.moveToFirst()){
            do{
                serviceRequest = new ServiceRequest();
                int serviceId = cursor.getInt(cursor.getColumnIndex("Service_ID"));
                Log.d("Service ID", String.valueOf(serviceId));
                serviceRequest.setServiceId(serviceId);
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setCategory(new ServiceCategory(cursor.getString(cursor.getColumnIndex("Category"))));
                serviceRequest.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                serviceRequest.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                serviceRequest.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                serviceRequest.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setReviewed(cursor.getInt(cursor.getColumnIndex("Is_Reviewed")) > 0);
                serviceRequest.setServiceTime(cursor.getString(cursor.getColumnIndex("Datetime")));

                //Set Bid
                ServiceBid bid = new ServiceBid();
                bid.setBidId(cursor.getInt(cursor.getColumnIndex("Bid_ID")));
                bid.setAmt(cursor.getDouble(cursor.getColumnIndex("Amount")));
                bid.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                bid.setHours(cursor.getInt(cursor.getColumnIndex("Hours")));
                bid.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                bid.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setPendingBid(bid);

                serviceRequests.add(serviceRequest);
            }while(cursor.moveToNext());
        }
        return serviceRequests;
    }

    public List<ServiceRequest> getConfirmedRequestsForVendor(int userId){
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT service_request.Service_ID, service_request.User_ID, service_request.Vendor_ID, service_request.Category, service_request.Datetime," +
                " service_request.Location, service_request.Title, service_request.Description, service_request.Status, service_request.Is_Reviewed," +
                " service_bids.Bid_ID, service_bids.Amount, service_bids.Notes, service_bids.Hours, service_bids.Status," +
                " user_account.last_name, user_account.first_name" +
                " FROM service_request" +
                " INNER JOIN service_bids" +
                " ON service_request.Service_ID=service_bids.Service_ID AND service_request.Vendor_ID=service_bids.Vendor_ID" +
                " LEFT JOIN user_account ON service_request.User_ID=user_account.user_id" +
                " WHERE service_request.Vendor_ID=?" +
                " and service_request.Status='Confirmed' and service_bids.Status='Accepted'";

        Log.d("SQL Query", query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if(cursor.moveToFirst()){
            do{
                serviceRequest = new ServiceRequest();
                serviceRequest.setServiceId(cursor.getInt(cursor.getColumnIndex("Service_ID")));
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory(new ServiceCategory(cursor.getString(cursor.getColumnIndex("Category"))));
                serviceRequest.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                serviceRequest.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                serviceRequest.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                serviceRequest.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setReviewed(cursor.getInt(cursor.getColumnIndex("Is_Reviewed")) > 0);
                serviceRequest.setServiceTime(cursor.getString(cursor.getColumnIndex("Datetime")));

                String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String name = lastName + ", " + firstName;
                serviceRequest.setRequestedBy(name);

                //Set Bid
                ServiceBid bid = new ServiceBid();
                bid.setBidId(cursor.getInt(cursor.getColumnIndex("Bid_ID")));
                bid.setAmt(cursor.getDouble(cursor.getColumnIndex("Amount")));
                bid.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                bid.setHours(cursor.getInt(cursor.getColumnIndex("Hours")));
                bid.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setWinningBid(bid);

                serviceRequests.add(serviceRequest);
            }while(cursor.moveToNext());
        }
        return serviceRequests;
    }

    public List<ServiceRequest> getCompletedRequestsForVendor(int userId){
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT service_request.Service_ID, service_request.User_ID, service_request.Vendor_ID, service_request.Category, service_request.Datetime," +
                " service_request.Location, service_request.Title, service_request.Description, service_request.Status as Service_Status, service_request.Is_Reviewed," +
                " service_bids.Bid_ID, service_bids.Amount, service_bids.Notes, service_bids.Hours, service_bids.Status," +
                " vendor_ratings.Rating, vendor_ratings.Comment," +
                " user_account.last_name, user_account.first_name" +
                " FROM service_request" +
                " INNER JOIN service_bids" +
                " ON service_request.Service_ID=service_bids.Service_ID AND service_request.Vendor_ID=service_bids.Vendor_ID" +
                " LEFT JOIN vendor_ratings ON service_request.Vendor_ID=vendor_ratings.Vendor_ID" +
                " LEFT JOIN user_account" +
                " ON service_request.User_ID=user_account.user_id" +
                " WHERE service_request.Vendor_ID=?" +
                " and service_request.Status='Completed' AND service_bids.Status='Accepted'";

        Log.d("SQL Query", query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if(cursor.moveToFirst()){
            do{
                serviceRequest = new ServiceRequest();
                int serviceId = cursor.getInt(cursor.getColumnIndex("Service_ID"));
                Log.d("Service ID", String.valueOf(serviceId));
                serviceRequest.setServiceId(serviceId);
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory(new ServiceCategory(cursor.getString(cursor.getColumnIndex("Category"))));
                serviceRequest.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                serviceRequest.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                serviceRequest.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                serviceRequest.setStatus(cursor.getString(cursor.getColumnIndex("Service_Status")));
                serviceRequest.setReviewed(cursor.getInt(cursor.getColumnIndex("Is_Reviewed")) > 0);
                serviceRequest.setServiceTime(cursor.getString(cursor.getColumnIndex("Datetime")));

                //Set customer
                String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String name = lastName + ", " + firstName;
                serviceRequest.setRequestedBy(name);

                //Set Bid
                ServiceBid bid = new ServiceBid();
                bid.setBidId(cursor.getInt(cursor.getColumnIndex("Bid_ID")));
                bid.setAmt(cursor.getDouble(cursor.getColumnIndex("Amount")));
                bid.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                bid.setHours(cursor.getInt(cursor.getColumnIndex("Hours")));
                bid.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setWinningBid(bid);

                //Set Rating
                if(serviceRequest.isReviewed()){
                    VendorRating vr = new VendorRating();
                    vr.setComment(cursor.getString(cursor.getColumnIndex("Comment")));
                    vr.setRating(cursor.getFloat(cursor.getColumnIndex("Rating")));
                    serviceRequest.setRating(vr);
                }
                serviceRequests.add(serviceRequest);
            }while(cursor.moveToNext());
        }
        return serviceRequests;
    }

    public List<ServiceRequest> getCompletedRequestsForCustomer(int userId) {
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT service_request.Service_ID, service_request.User_ID,"
                + "service_request.Vendor_ID, service_request.Category,"
                + "service_request.Datetime, service_request.Location,"
                + "service_request.Title, service_request.Description,"
                + "service_request.Status as Service_Status, service_request.Is_Reviewed,"
                + "service_bids.Bid_ID, service_bids.Amount, service_bids.Notes,"
                + "service_bids.Hours, service_bids.Status,"
                + "user_account.last_name, user_account.first_name,"
                + "vendor_ratings.Rating, vendor_ratings.Comment"
                + " FROM service_request"
                + " INNER JOIN service_bids"
                + " ON service_request.Service_ID=service_bids.Service_ID"
                + " AND service_request.Vendor_ID=service_bids.Vendor_ID"
                + " LEFT JOIN user_account"
                + " ON service_request.Vendor_ID=user_account.user_id"
                + " LEFT JOIN vendor_ratings ON service_request.Vendor_ID=vendor_ratings.Vendor_ID"
                + " WHERE service_request.User_ID=?"
                + " AND service_request.Status='Completed' AND service_bids.Status='Accepted'";

        Log.d("SQL Query", query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if (cursor.moveToFirst()) {
            do {
                serviceRequest = new ServiceRequest();
                serviceRequest.setServiceId(cursor.getInt(cursor.getColumnIndex("Service_ID")));
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory
                        (new ServiceCategory(cursor.getString(cursor.getColumnIndex("Category"))));
                serviceRequest.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                serviceRequest.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                serviceRequest.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                serviceRequest.setStatus(cursor.getString(cursor.getColumnIndex("Service_Status")));
                serviceRequest.setReviewed(cursor.getInt(cursor.getColumnIndex("Is_Reviewed")) > 0);
                serviceRequest.setServiceTime(cursor.getString(cursor.getColumnIndex("Datetime")));

                //Set Serviced By
                String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String name = lastName + ", " + firstName;
                serviceRequest.setServicedBy(name);

                //Set Winning Bid
                ServiceBid bid = new ServiceBid();
                bid.setBidId(cursor.getInt(cursor.getColumnIndex("Bid_ID")));
                bid.setAmt(cursor.getDouble(cursor.getColumnIndex("Amount")));
                bid.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                bid.setHours(cursor.getInt(cursor.getColumnIndex("Hours")));
                bid.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setWinningBid(bid);

                //Set Rating
                if(serviceRequest.isReviewed()){
                    VendorRating vr = new VendorRating();
                    vr.setComment(cursor.getString(cursor.getColumnIndex("Comment")));
                    vr.setRating(cursor.getFloat(cursor.getColumnIndex("Rating")));
                    serviceRequest.setRating(vr);
                }

                serviceRequests.add(serviceRequest);
            } while (cursor.moveToNext());
        }
        return serviceRequests;
    }

    public List<ServiceRequest> getConfirmedRequestsForCustomer(int userId) {
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT service_request.Service_ID, service_request.User_ID,"
                        + "service_request.Vendor_ID, service_request.Category,"
                        + "service_request.Datetime, service_request.Location,"
                        + "service_request.Title, service_request.Description,"
                        + "service_request.Status, service_request.Is_Reviewed,"
                        + "service_bids.Bid_ID, service_bids.Amount, service_bids.Notes,"
                        + "service_bids.Hours, service_bids.Status,"
                        + "user_account.last_name, user_account.first_name"
                        + " FROM service_request"
                        + " INNER JOIN service_bids"
                        + " ON service_request.Service_ID=service_bids.Service_ID"
                        + " AND service_request.Vendor_ID=service_bids.Vendor_ID"
                        + " LEFT JOIN user_account"
                        + " ON service_request.Vendor_ID=user_account.user_id"
                        + " WHERE service_request.User_ID=?"
                        + " AND service_request.Status='Confirmed' AND service_bids.Status='Accepted'";

        Log.d("SQL Query", query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if (cursor.moveToFirst()) {
            do {
                serviceRequest = new ServiceRequest();
                serviceRequest.setServiceId(cursor.getInt(cursor.getColumnIndex("Service_ID")));
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory
                        (new ServiceCategory(cursor.getString(cursor.getColumnIndex("Category"))));
                serviceRequest.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                serviceRequest.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                serviceRequest.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                serviceRequest.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setReviewed(cursor.getInt(cursor.getColumnIndex("Is_Reviewed")) > 0);
                serviceRequest.setServiceTime(cursor.getString(cursor.getColumnIndex("Datetime")));

                String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String name = lastName + ", " + firstName;
                serviceRequest.setServicedBy(name);

                //Set Bid
                ServiceBid bid = new ServiceBid();
                bid.setBidId(cursor.getInt(cursor.getColumnIndex("Bid_ID")));
                bid.setAmt(cursor.getDouble(cursor.getColumnIndex("Amount")));
                bid.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                bid.setHours(cursor.getInt(cursor.getColumnIndex("Hours")));
                bid.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setWinningBid(bid);

                serviceRequests.add(serviceRequest);
            } while (cursor.moveToNext());
        }
        return serviceRequests;
    }

    public List<ServiceRequest> getPendingRequestsForCustomer(int userId) {
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT service_request.Service_ID, service_request.User_ID,"
                        + "service_request.Vendor_ID, service_request.Category,"
                        + "service_request.Datetime, service_request.Location,"
                        + "service_request.Title, service_request.Description,"
                        + "service_request.Status, service_request.Is_Reviewed,"
                        + "service_bids.Bid_ID, service_bids.Amount, service_bids.Notes,"
                        + "service_bids.Hours, service_bids.Status,"
                        + "user_account.last_name, user_account.first_name,"
                        + "COUNT(CASE WHEN service_bids.Status='Pending' THEN 1 END) AS Num_Bids from service_request"
                        + " left join service_bids on service_request.Service_ID=service_bids.Service_ID"
                        + " natural join user_account"
                        + " where service_request.Status='Pending' and service_request.user_id=?"
                        + " group by service_request.Service_ID"
                        + " order by service_request.Service_ID desc";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if (cursor.moveToFirst()) {
            do {
                serviceRequest = new ServiceRequest();
                int serviceId = cursor.getInt(cursor.getColumnIndex("Service_ID"));
                Log.d("Service ID", String.valueOf(serviceId));
                serviceRequest.setServiceId(serviceId);
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory
                        (new ServiceCategory(cursor.getString(cursor.getColumnIndex("Category"))));
//                serviceRequest.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
                serviceRequest.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                serviceRequest.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                serviceRequest.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                serviceRequest.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setReviewed(cursor.getInt(cursor.getColumnIndex("Is_Reviewed")) > 0);
                serviceRequest.setServiceTime(cursor.getString(cursor.getColumnIndex("Datetime")));
                serviceRequest.setNumBids(cursor.getInt(cursor.getColumnIndex("Num_Bids")));
                String custLastName
                        = cursor.getString(cursor.getColumnIndex("last_name"));
                String custFirstName
                        = cursor.getString(cursor.getColumnIndex("first_name"));
                serviceRequest.setRequestedBy(custLastName + ", " + custFirstName);
                serviceRequests.add(serviceRequest);
            } while (cursor.moveToNext());
        }
//        Log.d("SQL Query Title", sr.getTitle());
        return serviceRequests;
    }

    public List<VendorRating> getRatingsForVendor(int vendorId){
        List<VendorRating> vendorRatings = new LinkedList<VendorRating>();
        String query = "select * from vendor_ratings"
                        + " left join user_account"
                        + " on vendor_ratings.User_ID=user_account.user_id"
                        + " natural join service_request"
                        + " where Vendor_ID=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(vendorId)});
        VendorRating vendorRating = null;
        if (cursor.moveToFirst()) {
            do {
                vendorRating = new VendorRating();
                vendorRating.setComment(cursor.getString(cursor.getColumnIndex("Comment")));
                vendorRating.setRating(cursor.getFloat(cursor.getColumnIndex("Rating")));

                // Set Reviewed By
                String custLastName
                        = cursor.getString(cursor.getColumnIndex("last_name"));
                String custFirstName
                        = cursor.getString(cursor.getColumnIndex("first_name"));
                vendorRating.setRequestedBy(custLastName + ", " + custFirstName);

                vendorRating.setServiceTitle(cursor.getString(cursor.getColumnIndex("Title")));
                vendorRatings.add(vendorRating);
            } while (cursor.moveToNext());
        }
        return vendorRatings;
    }

    public int getNewServiceId() {
        String query = "SELECT MAX(Service_ID) as service_id from service_request";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int serviceId = cursor.getInt(cursor.getColumnIndex("service_id"));
        return serviceId + 1;
    }

    public UserAccount getAccount(String username) {
        String user_type = "";
        UserAccount uc = null;
        open();
        Cursor cursor = db.rawQuery("Select * from user_account where username = ?",
                new String[]{username});
        if (cursor.moveToFirst()) {

            Log.d("SQL User found", "NEW USER");

            uc = new UserAccount();
            uc.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            uc.setUserType(cursor.getString(cursor.getColumnIndex("user_type")));
            uc.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            uc.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            uc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            uc.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
            uc.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
            uc.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            uc.setPoints(cursor.getInt(cursor.getColumnIndex("points")));
            uc.setRating(cursor.getDouble(cursor.getColumnIndex("rating")));

            Log.d("SQL USERNAME", uc.getUsername());
        }
        return uc;
    }

    public UserAccount getAccount(int userId) {
        String user_type = "";
        UserAccount uc = null;
        open();
        Cursor cursor = db.rawQuery("Select * from user_account " +
                        " natural join wallet" +
                        " where user_id = ?",
                new String[]{Integer.toString(userId)});
        if (cursor.moveToFirst()) {

            Log.d("SQL User found", "NEW USER");
            uc = new UserAccount();
            uc.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            uc.setUserType(cursor.getString(cursor.getColumnIndex("user_type")));
            uc.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            uc.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            uc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            uc.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
            uc.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
            uc.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            uc.setPoints(cursor.getInt(cursor.getColumnIndex("points")));
            uc.setRating(cursor.getDouble(cursor.getColumnIndex("rating")));
            uc.setWalletAmt(cursor.getDouble(cursor.getColumnIndex("Balance")));
            Log.d("SQL USERNAME", uc.getUsername());
        }
        return uc;
    }

    public void rejectBid(int bidId){
        ContentValues cv = new ContentValues();
        cv.put("Status","Rejected");
        db.update("service_bids", cv, "Bid_ID = ?", new String[]{Integer.toString(bidId)});
    }

    public void acceptBid(int bidId){
        ContentValues cv = new ContentValues();
        cv.put("Status","Accepted");
        db.update("service_bids", cv, "Bid_ID = ?", new String[]{Integer.toString(bidId)});
    }

    public void vendor_cancelBid(int bidId)
    {
        ContentValues cv = new ContentValues();
        cv.put("Status","Cancelled");
        db.update("service_bids", cv, "Bid_ID = ?", new String[]{Integer.toString(bidId)});
    }

    public void customer_invalidateBids(int serviceId){
        ContentValues cv = new ContentValues();
        cv.put("Status","Cancelled");
        db.update("service_bids", cv, "Service_ID = ?", new String[]{Integer.toString(serviceId)});
    }

    public void vendor_markServiceRequestAsComplete(int serviceId){
        ContentValues cv = new ContentValues();
        cv.put("Status","Completed");
        db.update("service_request", cv, "Service_ID = ?", new String[]{Integer.toString(serviceId)});
    }

    public void updateWalletBalance(int customerId, double newBalance){
        ContentValues cv = new ContentValues();
        cv.put("Balance", newBalance);
        db.update("wallet", cv, "User_ID = ?", new String[]{Integer.toString(customerId)});
    }

    public void setCustomerPoints(int userId, int numPoints){
        ContentValues cv = new ContentValues();
        cv.put("Points", numPoints);
        db.update("user_account", cv, "user_id = ?", new String[]{Integer.toString(userId)});
    }

    public void customer_cancelServiceRequest(int serviceId){
        ContentValues cv = new ContentValues();
        cv.put("Status", "Cancelled");
        db.update("service_request", cv, "Service_ID = ?", new String[]{Integer.toString(serviceId)});
    }

    public void setVendorToSeviceRequest(int vendorId, int serviceId){
        ContentValues cv = new ContentValues();
        cv.put("Vendor_ID",vendorId);
        cv.put("Status","Confirmed");
        db.update("service_request", cv, "Service_ID = ?", new String[]{Integer.toString(serviceId)});
    }

    public void setServiceRequestAsReviewed(int serviceId){
        ContentValues cv = new ContentValues();
        cv.put("Is_Reviewed", true);
        db.update("service_request", cv, "Service_ID = ?", new String[]{Integer.toString(serviceId)});
    }

    public boolean insertVendorRating(int vendorId,
                                      int userId,
                                      int serviceId,
                                      float rating,
                                      String comment){
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Vendor_ID", vendorId);
        contentValues.put("User_ID", userId);
        contentValues.put("Service_ID", serviceId);
        contentValues.put("Rating", rating);
        contentValues.put("Comment", comment);
        long ins = db.insert("vendor_ratings", null, contentValues);
        if (ins == -1) return false;
        else return true;
    }

    public void updateVendorRating(int vendorId){
        Cursor cursor
                = db.rawQuery("SELECT AVG(rating) as avg_rating FROM vendor_ratings WHERE Vendor_ID=?",
                               new String[]{String.valueOf(vendorId)});
        if (cursor.moveToFirst()){
            String value = cursor.getString(cursor.getColumnIndex("avg_rating"));
            if(value != null && value != ""){
                float rating = Float.parseFloat(value);
                ContentValues cv = new ContentValues();
                cv.put("rating", rating);
                db.update("user_account", cv, "user_id = ?",
                          new String[]{Integer.toString(vendorId)});
            }
        }
    }

    public boolean insertUser(String firstName,
                              String lastName,
                              String email,
                              String phoneNumber,
                              String username,
                              String password,
                              String userType,
                              String address,
                              String city,
                              String state,
                              String pincode,
                              String service,
                              String sampleRate,
                              String tasks) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", firstName);
        contentValues.put("last_name", lastName);
        contentValues.put("email", email);
        contentValues.put("phone", phoneNumber);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("user_type", userType);
        contentValues.put("address", address + "," + city + "," + state + "," + pincode);
        long ins = db.insert("user_account", null, contentValues);

        Cursor cursor = db.rawQuery("Select user_id from user_account where username = ?",
                new String[]{username});
        Integer userId = 0;
        if (cursor.moveToFirst())
            userId = cursor.getInt(cursor.getColumnIndex("user_id"));
        if (ins != -1 && userId > 0) {
            contentValues = new ContentValues();
            contentValues.put("User_ID", userId.toString());
            contentValues.put("Balance", 0);
            long walletIns = db.insert("wallet", null, contentValues);
            if (userType == "vendor") {
                contentValues = new ContentValues();
                contentValues.put("Vendor_ID", userId.toString());
                contentValues.put("Service", service);
                contentValues.put("Hourly_Rate", sampleRate);
                long vendSrvc = db.insert("vendor_services", null, contentValues);
            }
        }
        if (ins == -1) return false;
        else return true;
    }

    public boolean insertServiceBid(ServiceBid sb){
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Service_ID", sb.getServiceId());
        contentValues.put("Vendor_ID", sb.getVendorId());
        contentValues.put("Amount", sb.getAmt());
        contentValues.put("Notes", sb.getNotes());
        contentValues.put("Hours", sb.getHours());
        contentValues.put("Status", "Pending");
        long ins = db.insert("service_bids", null, contentValues);
        if (ins == -1) return false;
        else return true;
    }

    public boolean insertServiceRequest(ServiceRequest serviceRequest) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Service_ID", serviceRequest.getServiceId());
        contentValues.put("User_ID", serviceRequest.getCustomerId());
        contentValues.put("Vendor_ID", serviceRequest.getVendorId());
        contentValues.put("Category", serviceRequest.getCategory().getCategoryName());
        contentValues.put("Datetime", serviceRequest.getServiceTime());
        contentValues.put("Location", serviceRequest.getLocation());
        contentValues.put("Title", serviceRequest.getTitle());
        contentValues.put("Description", serviceRequest.getDescription());
        contentValues.put("Status", serviceRequest.getStatus());
        contentValues.put("Is_Reviewed", serviceRequest.isReviewed());
        long ins = db.insert("service_request", null, contentValues);
        if (ins == -1) return false;
        else return true;
    }

    //checking if credentials are valid
    public boolean authenticate(String username, String password) {
        open();
        Cursor cursor = db.rawQuery("Select * from user_account where username = ?" +
                        " and password= ?",
                new String[]{username, password});

        if (cursor.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    //checking if user exists;
    public boolean checkUser(String username) {
        open();
        Cursor cursor = db.rawQuery("Select * from user_account where username = ?",
                                    new String[]{username});

        if (cursor.getCount() > 0) return false;
        else return true;
    }
}
