package com.example.servemesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.MenuItemHoverListener;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class CustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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

        if(savedInstanceState == null)
        {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_home,
//                    new CreateServiceRequest()).commit();
//            navView.setCheckedItem(R.id.menuItem_customer_createServiceRequest);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_home,
                    new CustomerManageServiceRequests()).commit();
            navView.setCheckedItem(R.id.menuItem_customer_manageServiceRequest);
        }
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.menuItem_customer_createServiceRequest:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_home,
                        new CreateServiceRequest()).commit();
                break;
            case R.id.menuItem_customer_manageServiceRequest:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_home,
                        new CustomerManageServiceRequests()).commit();
                break;
            case R.id.menuItem_customer_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer_home,
                        new CustomerProfile()).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
