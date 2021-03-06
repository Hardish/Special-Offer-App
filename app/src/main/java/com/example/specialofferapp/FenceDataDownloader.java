package com.example.specialofferapp;


import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
class FenceDataDownloader extends AsyncTask<String, Void, String> {

    private Geocoder geocoder;
    private FenceMgr fenceMgr;
    private static final String FENCE_URL = "http://www.christopherhield.com/data/fences.json";

    FenceDataDownloader(MapsActivity mapsActivity, FenceMgr fenceMgr) {
        this.fenceMgr = fenceMgr;
        geocoder = new Geocoder(mapsActivity);
    }

    @Override
    protected void onPostExecute(String result) {

        if (result == null)
            return;

        ArrayList<FenceData> fences = new ArrayList<>();
        try {
            /*JSONObject jObj = new JSONObject(result);
            JSONArray jArr = jObj.getJSONArray("fences");
            for (int i = 0; i < jArr.length(); i++) {
                JSONObject fObj = jArr.getJSONObject(i);
                String id = fObj.getString("id");
                String address = fObj.getString("address");
                float rad = (float) fObj.getDouble("radius");
                int type = fObj.getInt("type");
                String color = fObj.getString("fenceColor");

                LatLng ll = getLatLong(address);

                if (ll != null) {
                    FenceData fd = new FenceData(id, ll.latitude, ll.longitude, address, rad, type, color);*/
            JSONObject jObj = new JSONObject(result);
            JSONArray jArr = jObj.getJSONArray("fences");
            for (int i = 0; i < jArr.length(); i++) {
                JSONObject fObj = jArr.getJSONObject(i);
                String id = fObj.getString("id");
                String address = fObj.getString("address");
                String web = fObj.getString("website");
                float rad = (float) fObj.getDouble("radius");
                int type = fObj.getInt("type");
                String message = fObj.getString("message");
                String code = fObj.getString("code");
                String fenceColor = fObj.getString("fenceColor");
                String logo = fObj.getString("logo");

                LatLng ll = getLatLong(address);
                //creating fence data
                if (ll != null) {
                    FenceData fd = new FenceData(id, ll.latitude, ll.longitude, address, web,rad,type,message,code,fenceColor,logo);
                    fences.add(fd);
                }
            }
            fenceMgr.addFences(fences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(FENCE_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private LatLng getLatLong(String address) {
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            Address a = addressList.get(0);
            return new LatLng(a.getLatitude(), a.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}