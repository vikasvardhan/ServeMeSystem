package com.example.servemesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    DatabaseAccess db;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(FirstFragment.PREFERENCES,
                Context.MODE_PRIVATE);

        TextView name = findViewById(R.id.admin_profile_name);
        TextView email = findViewById(R.id.admin_profile_email);
        TextView phone = findViewById(R.id.admin_profile_phone);
        TextView wallet = findViewById(R.id.admin_profile_balance);
        TextView location = findViewById(R.id.admin_profile_location);

        db = DatabaseAccess.getInstance(this);
        userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        UserAccount uc = db.getAccount(userId);
        if(uc != null)
        {
            name.setText(uc.getLastName() + ", " + uc.getFirstName());
            email.setText(uc.getEmail());
            phone.setText(uc.getPhone());
            wallet.setText("$" + String.valueOf(uc.getWalletAmt()));
            location.setText(uc.getAddress());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuItem_admin_options_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        SharedPreferences sharedpreferences = getSharedPreferences(FirstFragment.PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        editor.clear();
        editor.commit();
        startActivity(intent);
    }
}
