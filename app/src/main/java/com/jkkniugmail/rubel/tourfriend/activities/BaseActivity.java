package com.jkkniugmail.rubel.tourfriend.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;
import com.jkkniugmail.rubel.tourfriend.fragments.MapViewFragment;
import com.jkkniugmail.rubel.tourfriend.fragments.MyEvents;
import com.jkkniugmail.rubel.tourfriend.fragments.NearByFragment;
import com.jkkniugmail.rubel.tourfriend.fragments.WeatherView;
import com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments.MomentSaving;
import com.jkkniugmail.rubel.tourfriend.models.User;
import com.jkkniugmail.rubel.tourfriend.tasks.LocationProvider;
import com.jkkniugmail.rubel.tourfriend.utils.MySharedPreferences;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.jkkniugmail.rubel.tourfriend.Constants.DEFAULT_CITY;
import static com.jkkniugmail.rubel.tourfriend.Constants.DEFAULT_LATITUDE;
import static com.jkkniugmail.rubel.tourfriend.Constants.DEFAULT_LONGITUDE;


public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MapViewFragment.OnFragmentInteractionListener,
        MyEvents.OnMyEventInteractionListener,
        WeatherView.OnFragmentInteractionListener,LocationProvider.LocationCallback{

    private Geocoder geocoder;
    private LocationProvider locationProvider;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private int user_id;
    private User user;
    private DatabaseManager manager;
    private SearchView searchView;
    private String city_name;
    private Fragment fragment = null;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Intent intent = getIntent();
        user_id = intent.getIntExtra(Constants.USER_ID, 0);
        manager = new DatabaseManager(this);
        if (user_id != 0) {
            user = manager.getUser(user_id);
            if (user == null) {
                //error message and close
            }
        } else {
            //error message and close
        }


        locationProvider = new LocationProvider(this, this);

        city_name = DEFAULT_CITY;
        lat = DEFAULT_LATITUDE;
        lon = DEFAULT_LONGITUDE;


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setNavHeader(user);

        //attach fragment as starting

        fragmentManager = getSupportFragmentManager();
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_my_events));
    }

    @Override
    protected void onResume() {
        locationProvider.connect();
        super.onResume();
    }

    @Override
    protected void onPause() {
        locationProvider.disconnect();
        super.onPause();
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                fragment = WeatherView.newInstance(query);
                fragmentManager.beginTransaction().replace(R.id.content_base, fragment).commit();
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out) {
            MySharedPreferences preferences = new MySharedPreferences(this);
            preferences.unsaveUser();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_my_events:
                fragment = MyEvents.newInstance(user_id);

                break;
            case R.id.nav_weather:
                fragment = WeatherView.newInstance(city_name);

                break;
            case R.id.nav_map:
                fragment = MapViewFragment.newInstance(lat, lon, city_name);

                break;
            case R.id.nav_nearby:
                fragment = NearByFragment.newInstance(city_name, lat,lon);

                break;
            default:
                fragment = MyEvents.newInstance(user_id);
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.content_base, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMyEventInteraction() {
        //
    }




    public void setNavHeader(User user) {
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.head_name);
        TextView email = (TextView) header.findViewById(R.id.head_email);
        TextView phone = (TextView) header.findViewById(R.id.head_phone);
        name.setText(user.getFirst_name().toString() + " " + user.getLast_name().toString());
        email.setText(user.getEmail().toString());
        phone.setText(user.getPhone_no().toString());
    }

    @Override
    public void handleNewLocation(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();


        geocoder = new Geocoder(this, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            city_name = addresses.get(0).getLocality();
            Log.i("BaseActivity", "Location Found "+city_name+", latitude = "+lat+", longitude = "+lon);
            if (city_name==null){
                city_name = Constants.DEFAULT_CITY;
            }

            // latTv.setText(Double.toString(currentLatitude));

        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.i("BaseActivity", "Location Found "+city_name+", latitude = "+lat+", longitude = "+lon);

    }



    @Override
    public void onMomentCapturedParent(int event_id) {
        DialogFragment dialogFragment = MomentSaving.newInstance(event_id);
        dialogFragment.show(fragmentManager, "saveImage");
    }


}
