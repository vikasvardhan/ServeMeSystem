package com.example.servemesystem;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

     public DatabaseHelper(@Nullable Context context) {
        super(context, "SMS.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user_account(user_id integer primary key autoincrement, user_type text, username text, password text, email text, address text, first_name text, last_name text, phone text, points integer, rating float )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user_account");
    }

    //inserting in database
    public boolean insert(String firstName, String lastName, String email, String phoneNumber, String username, String password ){
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put("first_name", firstName);
         contentValues.put("last_name", lastName);
         contentValues.put("email", email);
         contentValues.put("phone", phoneNumber);
         contentValues.put("username", username);
         contentValues.put("password", password);
         long ins = db.insert("user_account", null, contentValues);
         if(ins == -1) return false;
         else return true;
    }

    //checking if user exists;
    public boolean checkUser(String username){
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor cursor = db.rawQuery("Select * from user_account where username = ?", new String[]{username});
         if(cursor.getCount() > 0) return false;
         else return true;
    }
}
