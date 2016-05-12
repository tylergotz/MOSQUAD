package com.mosquitosquad.foxcities.mosquad;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by Tyler Gotz on 5/2/2016.
 */
public class RouteSprays extends FragmentActivity implements LocationListener
{
    private LocationManager locationManager;
    private Context context;
    private GoogleMap googleMap;
    private String truck;
    private static final String GEOFIRE_REF = "https://mosquito-squad.firebaseio.com/truckLocations";
    GeoFire geoFire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_sprays);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapSprays);
        googleMap = mapFragment.getMap();
        geoFire = new GeoFire(new Firebase(GEOFIRE_REF));

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            truck = bundle.getString("truck");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        googleMap.clear();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Your Position"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        geoFire.setLocation(truck, new GeoLocation(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}




