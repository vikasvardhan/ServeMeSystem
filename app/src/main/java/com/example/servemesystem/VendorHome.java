package com.example.servemesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class VendorHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);

        Toolbar toolbar = findViewById(R.id.vendor_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.vendor_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                                                                  drawer,
                                                                  toolbar,
                                                                  R.string.navigation_drawer_open,
                                                                  R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new VendorViewServiceRequest()).commit();
            navView.setCheckedItem(R.id.menuItem_vendor_view_service_requests);
        }
    }

    public void logout(){
        SharedPreferences sharedpreferences
                = getSharedPreferences(FirstFragment.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        editor.clear();
        editor.commit();
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuItem_customer_options_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.menuItem_vendor_view_service_requests:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VendorViewServiceRequest()).commit();
                break;
            case R.id.menuItem_vendor_manage_service_requests:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VendorManageServiceRequests()).commit();
                break;
            case R.id.menuItem_vendor_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VendorProfile()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
