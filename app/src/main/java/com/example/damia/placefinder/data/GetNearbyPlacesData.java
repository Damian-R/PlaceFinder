package com.example.damia.placefinder.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by damia on 7/18/2017.
 */
public class GetNearbyPlacesData {
    private static GetNearbyPlacesData instance = null;
    public RequestQueue requestQueue;

    private final String URL_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private final String URL_LOCATION = "location=";
    private final String URL_RADIUS = "&radius=";
    private final String URL_TYPE = "&type=";
    private final String URL_SENSOR = "&sensor=true";
    private final String URL_KEY = "&key=AIzaSyA0MtUzpa2HeoONIHTt22T6P9SxTUXPbjA";

    public OnPlacesLoadedListener onPlacesLoadedListener;
    public interface OnPlacesLoadedListener{
        public void placesLoaded(ArrayList<HashMap<String, String>> list);
    }

    public static GetNearbyPlacesData getInstance(Context context) {
        if(instance == null){
            instance = new GetNearbyPlacesData(context);
        }
        return instance;
    }

    private GetNearbyPlacesData(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        onPlacesLoadedListener = (OnPlacesLoadedListener) context;

    }

    private String buildURL(Bundle bundle){
        String url = URL_BASE + URL_LOCATION + bundle.getString("location") + URL_RADIUS + bundle.getString("radius") + URL_TYPE + bundle.getString("type") + URL_SENSOR  + URL_KEY;
        Log.v("URL", url);
        return url;
    }

    ArrayList<HashMap<String, String>> placesList;

    public ArrayList<HashMap<String, String>> downloadPlacesData(Bundle bundle){
        String url = buildURL(bundle);

        placesList = new ArrayList<>();

        //TODO make more requests for the next pages of results since each page is limited to 20 places

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for(int i = 0; i < results.length(); i++){
                        HashMap<String, String> placeMap = new HashMap<>();
                        JSONObject main = results.getJSONObject(i);
                        JSONObject geo = main.getJSONObject("geometry");
                        JSONObject loc = geo.getJSONObject("location");
                        String name = main.getString("name");
                        String lat = loc.getString("lat");
                        String lng = loc.getString("lng");

                        placeMap.put("name", name);
                        placeMap.put("lat", lat);
                        placeMap.put("lng", lng);
                        Log.v("NAME", name);

                        placesList.add(placeMap);

                    }
                    onPlacesLoadedListener.placesLoaded(placesList);

                }catch (JSONException e){
                    Log.v("JSON", e.getLocalizedMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);

        return placesList;
    }

}
