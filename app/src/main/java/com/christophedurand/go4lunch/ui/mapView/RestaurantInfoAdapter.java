package com.christophedurand.go4lunch.ui.mapView;


import android.view.View;
import android.widget.TextView;

import com.christophedurand.go4lunch.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.net.PlacesClient;

import model.RestaurantDataAsyncTask;
import model.RestaurantPhotoAsyncTask;


public class RestaurantInfoAdapter implements GoogleMap.InfoWindowAdapter {

    //-- PROPERTIES
    private final View mRestaurantInfoView;
    private final PlacesClient placesClient;


    //-- CONSTRUCTOR
    public RestaurantInfoAdapter(View mRestaurantInfoView, PlacesClient placesClient) {
        this.mRestaurantInfoView = mRestaurantInfoView;
        this.placesClient = placesClient;
    }


    //-- METHODS
    @Override
    public View getInfoWindow(Marker marker) {
        // Define a Place ID.
        String placeId = (String) marker.getTag();

        new RestaurantDataAsyncTask(marker, placeId, placesClient).execute();

        new RestaurantPhotoAsyncTask(placeId, placesClient, mRestaurantInfoView).execute();

        TextView nameTextView = mRestaurantInfoView.findViewById(R.id.name_text_view);
        nameTextView.setText(marker.getTitle());

        TextView descriptionTextView = mRestaurantInfoView.findViewById(R.id.address_text_view);
        descriptionTextView.setText(marker.getSnippet());

        return mRestaurantInfoView;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


}
