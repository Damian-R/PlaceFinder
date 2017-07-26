package com.example.damia.placefinder.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.images.ImageManager;
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

    private final String URL_BASE_PLACES = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private final String URL_BASE_IMAGE = "https://maps.googleapis.com/maps/api/place/photo?";
    private final String URL_IMAGE_WIDTH = "maxwidth=300";
    private final String URL_IMAGE_HEIGHT = "&maxheight=300";
    private final String URL_LOCATION = "location=";
    private final String URL_RADIUS = "&radius=";
    private final String URL_TYPE = "&type=";
    private final String URL_OPEN = "&opennow=true";
    private final String URL_SENSOR = "&sensor=true";
    private final String URL_KEY = "&key=AIzaSyA0MtUzpa2HeoONIHTt22T6P9SxTUXPbjA";

    Context context;

    public OnPlacesLoadedListener onPlacesLoadedListener;

    public interface OnPlacesLoadedListener{
        void placesLoaded(ArrayList<Place> list);
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
        this.context = context;

    }

    private String buildPlaceURL(Bundle bundle){
        String url = URL_BASE_PLACES + URL_LOCATION + bundle.getString("location") + URL_RADIUS + bundle.getString("radius") + URL_TYPE + bundle.getString("type") + URL_SENSOR + URL_OPEN + URL_KEY;
        Log.v("URL", url);
        return url;
    }

    private String buildImgURL(String imgKey){
        String url = URL_BASE_IMAGE + URL_IMAGE_WIDTH + URL_IMAGE_HEIGHT + "&photoreference=" + imgKey + URL_KEY;
        Log.v("IMGURL", url);
        return url;
    }

    ArrayList<Place> placesList;

    public ArrayList<Place> downloadPlacesData(Bundle bundle){
        String url = buildPlaceURL(bundle);

        placesList = new ArrayList<>();

        //TODO make more requests for the next pages of results since each page is limited to 20 places

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for(int i = 0; i < results.length(); i++){
                        JSONObject main = results.getJSONObject(i);
                        JSONObject geo = main.getJSONObject("geometry");
                        JSONObject loc = geo.getJSONObject("location");
                        String placeID = main.getString("place_id");
                        String name = main.getString("name");
                        String lat = loc.getString("lat");
                        String lng = loc.getString("lng");
                        String address = main.getString("vicinity");
                        Double rating = null;
                        String photoID = null;

                        try {
                            if (main.getJSONArray("photos") != null) {
                                JSONArray photos = main.getJSONArray("photos");
                                photoID = photos.getJSONObject(0).getString("photo_reference");
                            }

                            if(main.getString("rating") != null){
                                rating = Double.parseDouble(main.getString("rating"));
                            }
                        }catch (JSONException e){
                            Log.v("JSON4", e.getLocalizedMessage());
                        }

                        Place place = new Place(photoID, name, placeID, Double.parseDouble(lat), Double.parseDouble(lng), address, rating, context);
                        Log.v("NAME", name);

                        placesList.add(place);

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

    public void downloadPlaceImage(String imageKey, final Place place){
        String url = buildImgURL(imageKey);
        place.setImageKey(imageKey);
        place.setPhotoURL(url);
    }
}
