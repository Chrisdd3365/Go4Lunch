package com.christophedurand.go4lunch.ui.mapView;


import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import model.pojo.RestaurantDetailsResponse;
import viewModel.RestaurantDetailsViewModel;
import viewModel.ViewModelFactory;


public class RestaurantInfoAdapter implements GoogleMap.InfoWindowAdapter {

    //-- PROPERTIES
    private final View mRestaurantInfoView;
    private final String mApiKey;
    private final LifecycleOwner mLifeCycleOwner;
    private final Fragment mFragment;


    //-- CONSTRUCTOR
    public RestaurantInfoAdapter(View mRestaurantInfoView,
                                 String mApiKey,
                                 LifecycleOwner mLifeCycleOwner,
                                 Fragment mFragment) {
        this.mRestaurantInfoView = mRestaurantInfoView;
        this.mApiKey = mApiKey;
        this.mLifeCycleOwner = mLifeCycleOwner;
        this.mFragment = mFragment;
    }


    //-- METHODS
    @Override
    public View getInfoWindow(Marker marker) {
        // Define a Place ID.
        String placeId = (String) marker.getTag();

        configureRestaurantDetailsViewModel().getRestaurantDetailsRepository(placeId, mApiKey).
                observe(mLifeCycleOwner, this::getRestaurantDetailsData);

        return mRestaurantInfoView;
    }

    private RestaurantDetailsViewModel configureRestaurantDetailsViewModel() {
        ViewModelFactory mViewModelFactory = ViewModelFactory.getInstance();
        return new ViewModelProvider(
                mFragment.getViewModelStore(), mViewModelFactory).get(RestaurantDetailsViewModel.class);
    }

    public void getRestaurantDetailsData(RestaurantDetailsResponse restaurantDetailsResponse) {

        TextView nameTextView = mRestaurantInfoView.findViewById(R.id.name_text_view);
        String name = restaurantDetailsResponse.getResult().name;
        nameTextView.setText(name);

        TextView descriptionTextView = mRestaurantInfoView.findViewById(R.id.address_text_view);
        String formattedAddress = restaurantDetailsResponse.getResult().formattedAddress;
        descriptionTextView.setText(formattedAddress);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

}
