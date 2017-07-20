package com.example.damia.placefinder.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.damia.placefinder.activities.MapsActivity;

/**
 * Created by damia on 7/20/2017.
 */

public class Place{
    Bitmap image;
    String photoID;
    String name;
    String id;
    Double lat;
    Double lng;

    GetNearbyPlacesData data;

    public Place(String photoID, String name, String id, Double lat, Double lng, Context context) {
        this.photoID = photoID;
        this.name = name;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        data = GetNearbyPlacesData.getInstance(context);
        data.downloadPlaceImage(this.photoID, this);
    }

    public Bitmap getImage() {
        return image;
    }

    public String getPhotoID() {
        return photoID;
    }

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

    public void setImage(Bitmap bitmap){
        this.image = bitmap;
    }

}