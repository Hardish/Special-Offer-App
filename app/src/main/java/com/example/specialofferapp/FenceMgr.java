package com.example.specialofferapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class FenceMgr {

    private static final String TAG = "FenceMgr";
    private MapsActivity mapsActivity;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private ArrayList<Circle> circles = new ArrayList<>();
    private List<PatternItem> pattern = Collections.<PatternItem>singletonList(new Dot());
    private static ArrayList<FenceData> fenceList = new ArrayList<>();

    FenceMgr(final MapsActivity mapsActivity) {
        this.mapsActivity =  mapsActivity;
        geofencingClient = LocationServices.getGeofencingClient(mapsActivity);

        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(mapsActivity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: removeGeofences");
                    }
                })
                .addOnFailureListener(mapsActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: removeGeofences");
                        Toast.makeText(mapsActivity, "Trouble removing existing fences: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        new FenceDataDownloader(mapsActivity, this).execute();
    }

    static FenceData getFenceData(String id) {
        for (FenceData fd : fenceList) {
            if (fd.getId().equals(id))
                return fd;
        }
        return null;
    }

    void drawFences() {

        for (FenceData fd: fenceList) {
            drawFence(fd);
        }
    }

    void eraseFences() {
        for (Circle c : circles)
            c.remove();
        circles.clear();
    }


    private void drawFence(FenceData fd) {

        int line = Color.parseColor(fd.getFenceColor());
        int fill = ColorUtils.setAlphaComponent(line, 85);

        LatLng latLng = new LatLng(fd.getLat(), fd.getLon());
        Circle c = mapsActivity.getMap().addCircle(new CircleOptions()
                .center(latLng)
                .radius(fd.getRadius())
                .strokePattern(pattern)
                .strokeColor(line)
                .fillColor(fill));

        circles.add(c);
    }

    void addFences(ArrayList<FenceData> fences) {

        fenceList.clear();
        fenceList.addAll(fences);

        for (FenceData fd : fenceList) {
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(fd.getId())
                    .setCircularRegion(
                            fd.getLat(),
                            fd.getLon(),
                            fd.getRadius())
                    .setTransitionTypes(fd.getType_fence())
                    .setExpirationDuration(Geofence.NEVER_EXPIRE) //Fence expires after N millis  -or- Geofence.NEVER_EXPIRE
                    .build();

            GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                    .addGeofence(geofence)
                    .build();

            geofencePendingIntent = getGeofencePendingIntent();

            geofencingClient
                    .addGeofences(geofencingRequest, geofencePendingIntent)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: addGeofences");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "onFailure: addGeofences");

                            Toast.makeText(mapsActivity, "Trouble adding new fence: " + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
        }
        drawFences();

    }

    private PendingIntent getGeofencePendingIntent() {

        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Intent intent = new Intent(mapsActivity, GeofenceBroadcastReceiver.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(mapsActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }



}
