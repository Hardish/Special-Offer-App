package com.example.specialofferapp;

import java.io.Serializable;

public class FenceData implements Serializable {

  private String id;
    private double lat;
    private double lon;
    private String address;
    private String weburl;
    private float radius;
    private int type_fence;
    private String code;
    private String fenceColor;
    private String logo;
    private String message;

    public FenceData(String id, double lat, double lon, String address, String weburl, float radius, int type_fence,String message ,String code, String fenceColor, String logo) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.address = address;
        this.weburl = weburl;
        this.radius = radius;
        this.type_fence = type_fence;
        this.code = code;
        this.message = message;
        this.fenceColor = fenceColor;
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getType_fence() {
        return type_fence;
    }

    public void setType_fence(int type_fence) {
        this.type_fence = type_fence;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFenceColor() {
        return fenceColor;
    }

    public void setFenceColor(String fenceColor) {
        this.fenceColor = fenceColor;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "FenceData{" +
                "id='" + id + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", address='" + address + '\'' +
                ", weburl='" + weburl + '\'' +
                ", radius=" + radius +
                ", type_fence=" + type_fence +
                ", code='" + code + '\'' +
                ", fenceColor='" + fenceColor + '\'' +
                ", logo='" + logo + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
