package com.example.servemesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings_vendor) {
//            Intent settingsIntent = new Intent(VendorHome.this, SettingsActivity.class);
//            startActivity(settingsIntent);
//        }

        return super.onOptionsItemSelected(item);
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
