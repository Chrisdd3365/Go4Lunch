package com.christophedurand.go4lunch.ui.mapView;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import model.pojo.Restaurant;

class MapViewState {
    @NonNull
    private final Location location;
    @Nullable
    private final List<Restaurant> restaurantsList;

    public MapViewState(@NonNull Location location, @Nullable List<Restaurant> restaurantsList) {
        this.location = location;
        this.restaurantsList = restaurantsList;
    }

    @NonNull
    public Location getLocation() {
        return location;
    }

    @Nullable
    public List<Restaurant> getRestaurantsList() {
        return restaurantsList;
    }

}
