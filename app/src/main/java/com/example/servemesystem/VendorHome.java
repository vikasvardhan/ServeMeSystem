package com.example.servemesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class VendorHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    SharedPreferences sharedpreferences;
    DatabaseAccess db;
    int userId;

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

        //set background image
        View header= navView.getHeaderView(0);
        LinearLayout sideNavLayout = (LinearLayout)header.findViewById(R.id.navHeaderVendor);
        sideNavLayout.setBackgroundResource(R.drawable.nav_menu_header);

        View headerView = navView.getHeaderView(0);
        TextView name = (TextView) headerView.findViewById(R.id.nav_header_vendor_name);
        TextView email = (TextView) headerView.findViewById(R.id.nav_header_vendor_email);
        sharedpreferences = getSharedPreferences(FirstFragment.PREFERENCES,
                Context.MODE_PRIVATE);

        db = DatabaseAccess.getInstance(this);
        userId = sharedpreferences.getInt(UserAccount.USERID, -1);
        UserAccount uc = db.getAccount(userId);
        if(uc != null){
            name.setText(uc.getLastName() + ", " + uc.getFirstName());
            email.setText(uc.getEmail());
        }

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new VendorManageServiceRequests()).commit();
            navView.setCheckedItem(R.id.menuItem_vendor_manage_service_requests);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_vendor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuItem_vendor_options_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment;
        switch (item.getItemId()){
            case R.id.menuItem_vendor_view_service_requests:
                currentFragment = new ServiceCategoryFragment("vendor");
                fragmentManager.beginTransaction()
                        .add(currentFragment, "service_category_vendor")
                        .addToBackStack("service_category_vendor")
                        .replace(R.id.fragment_container, currentFragment)
                        .commit();
                break;
            case R.id.menuItem_vendor_manage_service_requests:
                currentFragment = new VendorManageServiceRequests();
                fragmentManager.beginTransaction()
                        .add(currentFragment, "vendor_manage_service_requests")
                        .addToBackStack("vendor_manage_service_requests")
                        .replace(R.id.fragment_container, currentFragment)
                        .commit();
                break;
            case R.id.menuItem_vendor_profile:
                currentFragment = new VendorProfile();
                fragmentManager.beginTransaction()
                        .add(currentFragment, "vendor_profile")
                        .addToBackStack("vendor_profile")
                        .replace(R.id.fragment_container, currentFragment)
                        .commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
