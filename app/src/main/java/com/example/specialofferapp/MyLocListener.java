package com.example.specialofferapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocListener implements LocationListener {

    private MapsActivity mapsActivity;
    private static final String TAG = "MyLocListener";

    MyLocListener(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onProviderEnabled: "+"Just started for the location provider enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location);
        mapsActivity.updateLocation(location);
    }
}
