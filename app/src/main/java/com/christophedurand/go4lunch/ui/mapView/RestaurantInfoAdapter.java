package com.christophedurand.go4lunch.ui.mapView;


import android.view.View;
import android.widget.TextView;

import com.christophedurand.go4lunch.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class RestaurantInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mRestaurantInfoView;
    private final MapViewState mMapViewState;


    public RestaurantInfoAdapter(View mRestaurantInfoView,
                                 MapViewState mMapViewState) {
        this.mRestaurantInfoView = mRestaurantInfoView;
        this.mMapViewState = mMapViewState;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        getRestaurantDetailsData();

        return mRestaurantInfoView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void getRestaurantDetailsData() {
        TextView nameTextView = mRestaurantInfoView.findViewById(R.id.name_text_view);
        String name = mMapViewState.getMapMarker().getName();
        nameTextView.setText(name);

        TextView descriptionTextView = mRestaurantInfoView.findViewById(R.id.address_text_view);
        String formattedAddress = mMapViewState.getMapMarker().getAddress();
        descriptionTextView.setText(formattedAddress);
    }

}
