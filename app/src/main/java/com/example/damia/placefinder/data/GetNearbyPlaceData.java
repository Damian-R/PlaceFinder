package com.example.damia.placefinder.data;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.damia.placefinder.activities.MapsActivity;

import org.json.JSONObject;

/**
 * Created by Damian Reiter on 7/18/2017.
 */

public class GetNearbyPlaceData extends MapsActivity{
    private final String URL_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private final String URL_LOCATION = "location=";
    private final String URL_RADIUS = "&radius=";
    private final String URL_TYPE = "&type=";
    private final String URL_SENSOR = "&sensor=true";
    private final String URL_KEY = "&key=AIzaSyA0MtUzpa2HeoONIHTt22T6P9SxTUXPbjA";

    private String buildURL(Bundle bundle){
        String url = URL_BASE + URL_LOCATION + bundle.getString("location") + URL_RADIUS + bundle.getString("radius") + URL_TYPE + bundle.getString("type") + URL_SENSOR + URL_KEY;
        Log.v("URL", url);
        return url;
    }

    public void downloadPlacesData(Bundle bundle){
        String url = buildURL(bundle);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

}
