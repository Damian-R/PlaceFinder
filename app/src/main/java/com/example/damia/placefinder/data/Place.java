package com.example.damia.placefinder.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.damia.placefinder.activities.MapsActivity;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by damia on 7/20/2017.
 */

public class Place{
    String imageKey;
    String photoURL;
    String name;
    String id;
    String address;
    Double lat;
    Double lng;
    MarkerOptions options;

    GetNearbyPlacesData data;

    public Place(String photoURL, String name, String id, Double lat, Double lng, String address, Context context) {
        this.photoURL = photoURL;
        this.name = name;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        data = GetNearbyPlacesData.getInstance(context);
        data.downloadPlaceImage(this.photoURL, this);
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getImageKey(){ return imageKey ;}

    public MarkerOptions getMarkerOptions(){ return options; }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getAddress(){ return address; }

    public void setPhotoURL(String photoURL){
        this.photoURL = photoURL;
    }

    public void setImageKey(String imageKey){
        this.imageKey = imageKey;
    }

    public void setMarkerOptions(MarkerOptions options){
        this.options = options;
    }

}