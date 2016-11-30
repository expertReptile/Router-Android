package edu.csumb.cst438.router;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by expertReptile on 11/3/16.
 */

public class LocationService implements LocationListener {

    public static int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    //mininum distance moved until next update
    private static final long MIN_DISTANCE_CHANGE = 0; // changed to 0 for more constant updates 10 meters

    //minimum tiem in between updates in millisecons
    private static final long MIN_TIME_CHANGED = 0; // changed to 0 so that we can get more constant updates //60000; // 1000 ms = 1 sec. 1000 * 60 = 60000

    private final static boolean forceNetwork = false;

    public static LocationService instance = null;

    private LocationManager locationManager;
    public Location location;
    public double latitude;
    public  double longitude;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean locationServiceAvailable;
    Context context;
    private boolean locationChanged = false;

    //singleton
    public static LocationService getLocationManager(Context context) {
        if(instance == null) {
            instance = new LocationService(context);
        }
        return instance;
    }


    public LatLng getLocation() {
        this.locationChanged = false;
        Log.d("location", Double.toString(this.latitude));
        Log.d("location", Double.toString(this.longitude));
        return new LatLng(this.latitude, this.longitude);
    }

    public LocationService(Context mContext) {
        initLocationService(mContext);
        Log.d("GPS", "Location Service created.");
    }

    public LatLng getLastKnownLocation() {
        try {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return new LatLng(loc.getLongitude(), loc.getLatitude());
        }
        catch (SecurityException e) {
            Log.d("location", e.toString());
            try {
                Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                return new LatLng(loc.getLongitude(), loc.getLatitude());
            }
            catch (SecurityException e2) {
                Log.d("location", e2.toString());
            }
        }
        return getLocation();
    }

    public boolean hasChanged() {
        return locationChanged;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "Location Changed");
        this.locationChanged = true;
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        String msg = "New Latitude: " + this.latitude
                    + "New Longitude: " + this.longitude;
        Log.d("Location", "Changed: " + msg);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("GPS" , "Gps is turned on");
    }

    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(context, Settings.ACTION_LOCATION_SOURCE_SETTINGS.getClass());
        context.startActivity(intent);
        Log.d("GPS", "GPS is turned off");
    }

    public boolean isServiceAvailable(){
        return locationServiceAvailable;
    }

    public void initLocationService(Context mContext) {

        if(Build.VERSION.SDK_INT >= 23 &&
           ContextCompat.checkSelfPermission(Application.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
           ContextCompat.checkSelfPermission(Application.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity)mContext, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
            Log.d("GPS", "App does not have permissions");
            return;
        }

        try {
            this.latitude = 0.0;
            this.longitude = 0.0;
            this.locationManager = (LocationManager) Application.context.getSystemService(Context.LOCATION_SERVICE);

            //GPS & Network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(forceNetwork) isGPSEnabled = false;

            if(!isGPSEnabled && !isNetworkEnabled) {
                this.locationServiceAvailable = false;
            }

            else {
                this.locationServiceAvailable = true;
                if(locationManager != null) {
                    //checks to see if can start recording on network availability
                    if(isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_CHANGED,
                                MIN_DISTANCE_CHANGE, this);
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        //updateCoordinates();
                    }

                    //checks if it can start recording on gps availability
                    if(isGPSEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_CHANGED,
                                MIN_DISTANCE_CHANGE, this);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        //updateCoordinates();
                    }

                }
            }
        } catch (Exception e) {
            Log.d("GPS", "Error creating location service: " + e.getMessage());
        }

    }
}
