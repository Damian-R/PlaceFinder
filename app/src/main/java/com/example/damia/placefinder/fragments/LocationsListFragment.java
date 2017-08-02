package com.example.damia.placefinder.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.damia.placefinder.R;
import com.example.damia.placefinder.activities.MapsActivity;
import com.example.damia.placefinder.adapters.LocationsAdapter;
import com.example.damia.placefinder.data.Place;

import java.util.ArrayList;
import java.util.HashMap;


public class LocationsListFragment extends Fragment{

    private ArrayList<Place> placesList;
    public LocationsAdapter adapter;
    RecyclerView recyclerView;

    GetLocationsData getLocationsData;

    public interface GetLocationsData{
        ArrayList<Place> getList();
    }


    public LocationsListFragment() {
        // Required empty public constructor
    }

    public static LocationsListFragment newInstance() {
        LocationsListFragment fragment = new LocationsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLocationsData = (GetLocationsData) getContext();
        placesList = getLocationsData.getList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.getActivity().findViewById(R.id.container_locations).setVisibility(View.VISIBLE);
        View v = inflater.inflate(R.layout.fragment_locations_list, container, false);
        adapter = new LocationsAdapter(placesList, getContext());

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_locations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if(placesList.size() == 0){
            this.getActivity().findViewById(R.id.container_locations).setVisibility(View.GONE);
        }

        return v;
    }

    public void placesChanged(ArrayList<Place> places){
        adapter = new LocationsAdapter(places, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
