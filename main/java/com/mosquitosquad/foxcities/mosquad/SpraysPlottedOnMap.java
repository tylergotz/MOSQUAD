package com.mosquitosquad.foxcities.mosquad;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.os.Handler;
import android.widget.Button;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler Gotz on 4/30/2016.
 */
public class SpraysPlottedOnMap extends FragmentActivity implements GeoQueryEventListener, GoogleMap.OnCameraChangeListener
{
    private static final GeoLocation INTIAL_CENTER = new GeoLocation(44.235142, -88.422241);
    private static final int INTIAL_ZOOM_LEVEL = 10;
    private static final String GEO_FIRE_REF = "https://mosquito-squad.firebaseio.com/truckLocations";
    private static final String FIRE_REF = "https://mosquito-squad.firebaseio.com";

    private GoogleMap googleMap;
    private Circle searchCircle;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private String date;
    private String[] list1, list2, list3;
    private ArrayList<String> sprays;
    private Map<String, Marker> markers;
    boolean comesFromList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plotted_sprays);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        this.googleMap = mapFragment.getMap();
        LatLng center = new LatLng(INTIAL_CENTER.latitude, INTIAL_CENTER.longitude);
        this.searchCircle = this.googleMap.addCircle(new CircleOptions().center(center).radius(1000));
        searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
        searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, INTIAL_ZOOM_LEVEL));
        googleMap.setOnCameraChangeListener(this);
        Firebase.setAndroidContext(this);

        geoFire = new GeoFire(new Firebase(GEO_FIRE_REF));
        geoQuery = geoFire.queryAtLocation(INTIAL_CENTER, 1);
        markers = new HashMap<>();

        final Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            date = bundle.getString("date");
            list1 = bundle.getStringArray("spraysForTruck1");
            list2 = bundle.getStringArray("spraysForTruck2");
            list3 = bundle.getStringArray("spraysForTruck3");
        }

        Bundle bundle1 = getIntent().getExtras();
        if(bundle1 != null)
        {
            comesFromList = bundle1.getBoolean("comesFromList");
            sprays = bundle1.getStringArrayList("list");
        }



        Firebase firebase = new Firebase(FIRE_REF);
        Query query = firebase.child("users").child("clients");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] values = dataSnapshot.getValue().toString().split("=");
                for(int i = 0; i < values.length; i++)
                {
                    if(comesFromList == true)
                    {
                        String[] customerList = sprays.toArray(new String[sprays.size()]);
                        for(int x = 0; x < customerList.length; x++)
                        {
                            String[] getName = customerList[x].split("-");
                            if (values[i].contains(getName[0].substring(0, getName[0].length() - 1)))
                            {
                                String address = values[i+1].substring(0, values[i+1].indexOf(","))
                                        + " " + values[i + 2].substring(0, values[i + 2].indexOf(","))
                                        + " " + values[i + 4].substring(0, values[i+4].indexOf(","))
                                        + " " + values[i - 1].substring(0, values[i-1].indexOf(","));
                                getLatLongFromPlace(address, getName[0].substring(0, getName[0].length() - 1));
                            }
                        }
                    }
                    else
                    {
                        for(int j = 0; j < list1.length; j++)
                        {
                            if(values[i].contains(list1[j].substring(0, list1[j].indexOf("("))))
                            {
                                String address = values[i+1].substring(0, values[i+1].indexOf(","))
                                        + " " + values[i + 2].substring(0, values[i + 2].indexOf(","))
                                        + " " + values[i + 4].substring(0, values[i+4].indexOf(","))
                                        + " " + values[i - 1].substring(0, values[i-1].indexOf(","));
                                getLatLongFromPlace(address, list1[j].substring(0, list1[j].indexOf("(")));
                            }
                        }

                        for(int k = 0; k < list2.length; k++)
                        {
                            if(values[i].contains(list2[k].substring(0, list2[k].indexOf("("))))
                            {
                                String address = values[i+1].substring(0, values[i+1].indexOf(","))
                                        + " " + values[i + 2].substring(0, values[i + 2].indexOf(","))
                                        + " " + values[i + 4].substring(0, values[i+4].indexOf(","))
                                        + " " + values[i - 1].substring(0, values[i-1].indexOf(","));
                                getLatLongFromPlace(address, list2[k].substring(0, list2[k].indexOf("(")));
                            }
                        }

                        for(int m = 0; m < list3.length; m++)
                        {
                            if (values[i].contains(list3[m].substring(0, list3[m].indexOf("("))))
                            {
                                String address = values[i+1].substring(0, values[i+1].indexOf(","))
                                        + " " + values[i + 2].substring(0, values[i + 2].indexOf(","))
                                        + " " + values[i + 4].substring(0, values[i+4].indexOf(","))
                                        + " " + values[i - 1].substring(0, values[i-1].indexOf(","));
                                getLatLongFromPlace(address, list3[m].substring(0, list3[m].indexOf("(")));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        geoQuery.removeAllListeners();
        for(Marker marker : markers.values())
        {
            marker.remove();
        }
        markers.clear();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        geoQuery.addGeoQueryEventListener(this);
    }

    @Override
    public void onKeyEntered(String key, GeoLocation geoLocation)
    {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(geoLocation.latitude, geoLocation.longitude))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            .title("Truck Location"));
        markers.put(key, marker);
    }

    @Override
    public void onKeyExited(String key)
    {
        Marker marker = markers.get(key);
        if(marker != null)
        {
            marker.remove();
            markers.remove(key);
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location)
    {
        Marker marker = markers.get(key);
        if(marker != null)
        {
            animateMarkerTo(marker, location.latitude, location.longitude);
        }
    }

    @Override
    public void onGeoQueryReady()
    {

    }

    @Override
    public void onGeoQueryError(FirebaseError error)
    {
        new AlertDialog.Builder(this).setTitle("Error").setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final android.view.animation.Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed / DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private double zoomLevelToRadius(double zoomLevel)
    {
        return 16384000 / Math.pow(2, zoomLevel);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition)
    {
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        searchCircle.setCenter(center);
        searchCircle.setRadius(radius);
        geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        geoQuery.setRadius(radius/1000);
    }

    public void getLatLongFromPlace(String place, String truck)
    {
        try
        {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses;
            addresses = geocoder.getFromLocationName(place, 5);

            if(addresses != null)
            {
                Address location = addresses.get(0);
                double lat= location.getLatitude();
                double lng = location.getLongitude();
                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(truck));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
