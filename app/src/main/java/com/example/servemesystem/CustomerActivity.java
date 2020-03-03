package com.example.servemesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.MenuItemHoverListener;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;

public class CustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.customer_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.nav_view_customer);
        navView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_home,
//                    new CreateServiceRequest()).commit();
//            navView.setCheckedItem(R.id.menuItem_customer_createServiceRequest);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_home,
                    new CustomerManageServiceRequests()).commit();
            navView.setCheckedItem(R.id.menuItem_customer_manageServiceRequest);
        }

//        findViewById(R.id.menuItem_customer_options_logout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout(v);
//            }
//        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_customer, menu);
        return true;
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem_customer_createServiceRequest:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_customer_home, new ServiceCategoryFragment())
                        .commit();
                break;
            case R.id.menuItem_customer_manageServiceRequest:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_home,
                        new CustomerManageServiceRequests()).commit();
                break;
            case R.id.menuItem_customer_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_home,
                        new CustomerProfile()).commit();
                break;
            case R.id.action_settings_customer:
                Intent settingsIntent = new Intent(CustomerActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
