package com.example.damia.placefinder.holders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.request.RequestListener;
import com.example.damia.placefinder.R;
import com.example.damia.placefinder.activities.MapsActivity;
import com.example.damia.placefinder.adapters.LocationsAdapter;
import com.example.damia.placefinder.data.GetNearbyPlacesData;
import com.example.damia.placefinder.data.Place;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by damia on 7/20/2017.
 */

public class LocationsViewHolder extends RecyclerView.ViewHolder{

    private ImageView locationImage;
    private ImageView starOne;
    private ImageView starTwo;
    private ImageView starThree;
    private ImageView starFour;
    private ImageView starFive;


    private TextView locationName;
    private TextView locationAddress;
    private TextView locationRating;

    public LocationsViewHolder(View itemView) {
        super(itemView);

        locationName = (TextView)itemView.findViewById(R.id.location_name);
        locationAddress = (TextView)itemView.findViewById(R.id.location_address);
        locationImage = (ImageView) itemView.findViewById(R.id.location_image);
        locationRating = (TextView)itemView.findViewById(R.id.rating_text);

        starOne = (ImageView) itemView.findViewById(R.id.star_1);
        starTwo = (ImageView) itemView.findViewById(R.id.star_2);
        starThree = (ImageView) itemView.findViewById(R.id.star_3);
        starFour = (ImageView) itemView.findViewById(R.id.star_4);
        starFive = (ImageView) itemView.findViewById(R.id.star_5);
    }

    public void updateUI(final Place place, Context context, final LocationsAdapter.OnItemClickListener listener){
        locationName.setText(place.getName());
        locationAddress.setText(place.getAddress());

        if(place.getRating() != null) {
            locationRating.setText(place.getRating().toString());
            Double rating = place.getRating();
            updateStarRatings(rating, context);
        } else {
            locationRating.setText("No Ratings");
            locationRating.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            starOne.setVisibility(View.INVISIBLE);
            starTwo.setVisibility(View.INVISIBLE);
            starThree.setVisibility(View.INVISIBLE);
            starFour.setVisibility(View.INVISIBLE);
            starFive.setVisibility(View.INVISIBLE);
        }

        if(place.getImageKey() != null) { // if the place has an image
            Glide.with(context).load(place.getPhotoURL()).into(locationImage); // set the image in RecyclerView using glide
        }else {
            locationImage.setImageDrawable(context.getResources().getDrawable(R.drawable.no_image)); // if there is no image, notify the user
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(place);
            }
        });
    }

    private void updateStarRatings(Double rating, Context context){
        if(rating > 0.3){
            Glide.with(context).load(R.drawable.star_half).into(starOne);
            if(rating > 0.8){
                Glide.with(context).load(R.drawable.star).into(starOne);
                if(rating > 1.3){
                    Glide.with(context).load(context.getResources().getDrawable(R.drawable.star_half)).into(starTwo);
                    if(rating > 1.8){
                        Glide.with(context).load(R.drawable.star).into(starTwo);
                        if(rating > 2.3){
                            Glide.with(context).load(R.drawable.star_half).into(starThree);
                            if(rating > 2.8){
                                Glide.with(context).load(R.drawable.star).into(starThree);
                                if(rating > 3.3){
                                    Glide.with(context).load(R.drawable.star_half).into(starFour);
                                    if(rating > 3.8){
                                        Glide.with(context).load(R.drawable.star).into(starFour);
                                        if(rating > 4.3){
                                            Glide.with(context).load(R.drawable.star_half).into(starFive);
                                            if(rating > 4.8){
                                                Glide.with(context).load(R.drawable.star).into(starFive);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
