package com.example.damia.placefinder.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.TestMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damia.placefinder.R;
import com.example.damia.placefinder.adapters.LocationsAdapter;
import com.example.damia.placefinder.data.GetNearbyPlacesData;
import com.example.damia.placefinder.data.Place;
import com.example.damia.placefinder.fragments.LocationsListFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener
, GetNearbyPlacesData.OnPlacesLoadedListener, LocationsListFragment.GetLocationsData, LocationsAdapter.OnItemClickListener{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationMenu;
    private SeekBar seekBar;
    private TextView radiusTxt;

    public FrameLayout locationsContainer;

    final int PERMISSION_LOCATION_CODE = 1;

    private MarkerOptions userMarker;
    private Bundle URLInfo;

    private final String MAP_RADIUS = "500";

    GetNearbyPlacesData data;
    ArrayList<Place> placeList;

    LocationsListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initializeMap();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        data = GetNearbyPlacesData.getInstance(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        navigationMenu = (NavigationView) findViewById(R.id.navigation_menu);
        View headerView = navigationMenu.inflateHeaderView(R.layout.navigation_header);
        locationsContainer = (FrameLayout)findViewById(R.id.container_locations);
        seekBar = (SeekBar)headerView.findViewById(R.id.seekBar);
        radiusTxt = (TextView)headerView.findViewById(R.id.radiusTxt);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusTxt.setText("RADIUS: " + (progress * 20 + 500));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerLayout.bringToFront();
                mDrawerLayout.requestLayout();
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                navigationMenu.bringToFront();
                return super.onOptionsItemSelected(item);
            }
        };

        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationMenu.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.v("NAV", "Navigation item selected");
        URLInfo = new Bundle();
        URLInfo.putString("radius", "" + (seekBar.getProgress() * 20 + 500));
        URLInfo.putString("location", userMarker.getPosition().latitude + "," + userMarker.getPosition().longitude);

        mMap.clear();
        switch (item.getItemId()){
            case R.id.nav_restaurant: {
                URLInfo.putString("type", "restaurant");
                break;
            }
            case R.id.nav_pharmacy: {
                URLInfo.putString("type", "pharmacy");
                break;
            }
            case R.id.nav_bank: {
                URLInfo.putString("type", "bank");
                break;
            }
            case R.id.nav_library: {
                URLInfo.putString("type", "library");
                break;
            }
        }
        placeList = data.downloadPlacesData(URLInfo);

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_CODE);
        } else {
            startLocationServices();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_LOCATION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocationServices();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        userMarker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
        mMap.addMarker(userMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userMarker.getPosition(), 15));
    }

    public void initializeMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void startLocationServices(){
        try{
            LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);
        }catch(SecurityException e){
            Log.v("SECURITY E", e.getLocalizedMessage());
        }
    }

    @Override
    public void placesLoaded(ArrayList<Place> list) {
        Log.v("PLACES", "adding places");
        MarkerOptions markerOptions = new MarkerOptions();

        // build a resource path using the type of place
        String type = URLInfo.getString("type");
        String resourcePath = "drawable/pin_" + type;

        // create a resource path out of the String created
        int resource = getResources().getIdentifier(resourcePath, null, this.getPackageName());

        // set the marker icon according to the place type
        markerOptions.icon(BitmapDescriptorFactory.fromResource(resource));

        for(int i = 0; i < list.size(); i++){
            // get the latitude and longitude
            double lat = list.get(i).getLat();
            double lng = list.get(i).getLng();
            // set the marker position and title
            markerOptions.position(new LatLng(lat, lng));
            markerOptions.title(list.get(i).getName());
            list.get(i).setMarkerOptions(markerOptions);
            mMap.addMarker(markerOptions);
        }
        createLocationsListFragment();
    }

    private void createLocationsListFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if(fragment == null) {
            fragment = LocationsListFragment.newInstance();
            fm.beginTransaction().add(R.id.container_locations, fragment).commit();
            Log.v("FRAGMENT", "new fragment created");
        } else {
            fragment.placesChanged(placeList);
        }
    }

    @Override
    public ArrayList<Place> getList() {
        return placeList;
    }

    @Override
    public void onItemClick(Place place) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLat() - 0.003, place.getLng())));
    }
}
