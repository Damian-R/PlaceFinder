package com.example.damia.placefinder.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.damia.placefinder.R;
import com.example.damia.placefinder.holders.LocationsViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by damia on 7/20/2017.
 */

public class LocationsAdapter extends RecyclerView.Adapter<LocationsViewHolder>{

    ArrayList<HashMap<String, String>> locations;

    public LocationsAdapter(ArrayList<HashMap<String, String>> locations) {
        this.locations = locations;
    }

    @Override
    public LocationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_location, parent, false);
        return new LocationsViewHolder(card);
    }

    @Override
    public void onBindViewHolder(LocationsViewHolder holder, int position) {
        HashMap<String, String> location = locations.get(position);
        holder.updateUI(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

}
