package com.example.servemesystem;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "SMS.db";
    public static int DB_VERSION = 1;

     public DatabaseHelper(@Nullable Context context) {
         super(context, "SMS.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user_account(user_id integer primary key autoincrement, user_type text, username text, password text, email text, address text, first_name text, last_name text, phone text, points integer, rating float )");

        // insert test customer account
        db.execSQL("INSERT INTO user_account values(NULL, " +
                                                    "'customer', " +
                                                    "'testcust', " +
                                                    "'Test123$', " +
                                                    "'testcust@gmail.com', " +
                                                    "NULL, " +
                                                    "'Test'," +
                                                    "'Customer'," +
                                                    "'111-111-1111'," +
                                                    "NULL," +
                                                    "NULL)");

        // insert test vendor account
        db.execSQL("INSERT INTO user_account values(NULL, " +
                                                    "'vendor', " +
                                                    "'testvend', " +
                                                    "'Test123$', " +
                                                    "'testvend@gmail.com', " +
                                                    "NULL, " +
                                                    "'Test'," +
                                                    "'Vendor'," +
                                                    "'222-222-2222'," +
                                                    "NULL," +
                                                    "NULL)");
    }

    public String getAccountType(String username){
        String user_type = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select user_type from user_account where username = ?",
                                    new String[]{username});
        if (cursor.moveToFirst()) {
            user_type = cursor.getString(cursor.getColumnIndex("user_type"));
        }
//        user_type = cursor.getString( cursor.getColumnIndex("user_type") );
        return user_type;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user_account");
    }

    //inserting in database
    public boolean insertUser(String firstName,
                              String lastName,
                              String email,
                              String phoneNumber,
                              String username,
                              String password,
                              String userType){
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put("first_name", firstName);
         contentValues.put("last_name", lastName);
         contentValues.put("email", email);
         contentValues.put("phone", phoneNumber);
         contentValues.put("username", username);
         contentValues.put("password", password);
         contentValues.put("user_type", userType);
         long ins = db.insert("user_account", null, contentValues);
         if(ins == -1) return false;
         else return true;
    }

    //checking if credentials are valid
    public boolean authenticate(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user_account where username = ?" +
                                        " and password= ?",
                                    new String[]{username, password});
        if(cursor.getCount() == 1) {
            return true;
        }
        else{
            return false;
        }
    }

    //checking if user exists;
    public boolean checkUser(String username){
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor cursor = db.rawQuery("Select * from user_account where username = ?", new String[]{username});
         if(cursor.getCount() > 0) return false;
         else return true;
    }
}
