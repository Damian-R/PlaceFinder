package com.example.damia.placefinder.holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.damia.placefinder.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by damia on 7/20/2017.
 */

public class LocationsViewHolder extends RecyclerView.ViewHolder{

    private ImageView locationImage;
    private TextView locationName;
    private TextView locationAddress;

    public LocationsViewHolder(View itemView) {
        super(itemView);

        locationName = (TextView)itemView.findViewById(R.id.location_name);
        locationAddress = (TextView)itemView.findViewById(R.id.location_address);
        locationImage = (ImageView) itemView.findViewById(R.id.location_image);
    }

    public void updateUI(HashMap<String, String > place){
        locationName.setText(place.get("name"));
    }

}
