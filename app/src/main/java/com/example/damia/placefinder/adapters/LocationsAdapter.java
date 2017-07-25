package com.example.damia.placefinder.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.damia.placefinder.R;
import com.example.damia.placefinder.data.GetNearbyPlacesData;
import com.example.damia.placefinder.data.Place;
import com.example.damia.placefinder.holders.LocationsViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by damia on 7/20/2017.
 */

public class LocationsAdapter extends RecyclerView.Adapter<LocationsViewHolder>{

    public interface OnItemClickListener{
        void onItemClick(Place place);
    }

    private ArrayList<Place> locations;
    private Context context;
    private GetNearbyPlacesData data;
    private OnItemClickListener listener;

    public LocationsAdapter(ArrayList<Place> locations, Context context) {
        this.locations = locations;
        this.context = context;
        data = GetNearbyPlacesData.getInstance(context);
    }

    @Override
    public LocationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_location, parent, false);
        listener = (OnItemClickListener) context;
        return new LocationsViewHolder(card);
    }

    @Override
    public void onBindViewHolder(LocationsViewHolder holder, int position) {
        Place location = locations.get(position);
        holder.updateUI(location, context, listener);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

}
