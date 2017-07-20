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

    private static final String ARG_PARAM1 = "param1";

    private ArrayList<Place> mParam1;
    public LocationsAdapter adapter;

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
        mParam1 = getLocationsData.getList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_locations_list, container, false);
        adapter = new LocationsAdapter(mParam1, getContext());

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recycler_locations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        return v;
    }

}
