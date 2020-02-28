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

    public List<ServiceRequest> getConfirmedRequestsForVendor(int userId){
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT * from service_request"
                + " WHERE Vendor_ID=?"
                + " AND Status='Confirmed'";

        Log.d("SQL Query", query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if(cursor.moveToFirst()){
            do{
                serviceRequest = new ServiceRequest();
                serviceRequest.setServiceId(cursor.getInt(cursor.getColumnIndex("Service_ID")));
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
                serviceRequest.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                serviceRequest.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                serviceRequest.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                serviceRequest.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setReviewed(cursor.getInt(cursor.getColumnIndex("Is_Reviewed")) > 0);
                serviceRequest.setServiceTime(cursor.getString(cursor.getColumnIndex("Datetime")));
                serviceRequests.add(serviceRequest);
            }while(cursor.moveToNext());
        }
        for(ServiceRequest sr: serviceRequests)
        {
            Log.d("SQL Query Category", sr.getCategory());
            Log.d("SQL Query Title", sr.getTitle());
        }
        return serviceRequests;
    }

    public List<ServiceRequest> getCompletedRequestsForCustomer(int userId){
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT * from service_request"
                + " LEFT JOIN user_account"
                + " ON service_request.Vendor_ID=user_account.user_id"
                + " WHERE service_request.User_ID=?"
                + " AND service_request.Status='Completed'";

        Log.d("SQL Query", query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if(cursor.moveToFirst()){
            do{
                serviceRequest = new ServiceRequest();
                serviceRequest.setServiceId(cursor.getInt(cursor.getColumnIndex("Service_ID")));
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
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
                serviceRequests.add(serviceRequest);
            }while(cursor.moveToNext());
        }
        for(ServiceRequest sr: serviceRequests)
        {
            Log.d("SQL Query Category", sr.getCategory());
            Log.d("SQL Query Title", sr.getTitle());
        }
        return serviceRequests;
    }

    public List<ServiceRequest> getConfirmedRequestsForCustomer(int userId){
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "SELECT * from service_request"
                + " LEFT JOIN user_account"
                + " ON service_request.Vendor_ID=user_account.user_id"
                + " WHERE service_request.User_ID=?"
                + " AND service_request.Status='Confirmed'";

        Log.d("SQL Query", query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if(cursor.moveToFirst()){
            do{
                serviceRequest = new ServiceRequest();
                serviceRequest.setServiceId(cursor.getInt(cursor.getColumnIndex("Service_ID")));
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
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
                serviceRequests.add(serviceRequest);
            }while(cursor.moveToNext());
        }
        for(ServiceRequest sr: serviceRequests)
        {
            Log.d("SQL Query Category", sr.getCategory());
            Log.d("SQL Query Title", sr.getTitle());
        }
        return serviceRequests;
    }

    public List<ServiceRequest> getPendingRequestsForCustomer(int userId){
        List<ServiceRequest> serviceRequests = new LinkedList<ServiceRequest>();
        String query = "select * from service_request"
                        + " where User_ID=?"
                        + " and Status='Pending'";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        ServiceRequest serviceRequest = null;
        if(cursor.moveToFirst()){
            do{
                serviceRequest = new ServiceRequest();
                serviceRequest.setServiceId(cursor.getInt(cursor.getColumnIndex("Service_ID")));
                serviceRequest.setCustomerId(cursor.getInt(cursor.getColumnIndex("User_ID")));
                serviceRequest.setVendorId(cursor.getInt(cursor.getColumnIndex("Vendor_ID")));
                serviceRequest.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
                serviceRequest.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                serviceRequest.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                serviceRequest.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                serviceRequest.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                serviceRequest.setReviewed(cursor.getInt(cursor.getColumnIndex("Is_Reviewed")) > 0);
                serviceRequest.setServiceTime(cursor.getString(cursor.getColumnIndex("Datetime")));
                serviceRequests.add(serviceRequest);
            }while(cursor.moveToNext());
        }
//        Log.d("SQL Query Title", sr.getTitle());
        return serviceRequests;
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

    public boolean insertUser(String firstName,
                              String lastName,
                              String email,
                              String phoneNumber,
                              String username,
                              String password,
                              String userType) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", firstName);
        contentValues.put("last_name", lastName);
        contentValues.put("email", email);
        contentValues.put("phone", phoneNumber);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("user_type", userType);
        long ins = db.insert("user_account", null, contentValues);

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
        Cursor cursor = db.rawQuery("Select * from user_account where username = ?", new String[]{username});

        if (cursor.getCount() > 0) return false;
        else return true;
    }
}
