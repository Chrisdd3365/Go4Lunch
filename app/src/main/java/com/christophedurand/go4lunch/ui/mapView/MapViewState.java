package com.christophedurand.go4lunch.ui.mapView;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.christophedurand.go4lunch.model.pojo.Restaurant;


class MapViewState {

    @NonNull
    private final Location location;
    @Nullable
    private final List<Restaurant> restaurantsList;
    private final List<MapMarker> mapMarkersList;


    public MapViewState(@NonNull Location location, @Nullable List<Restaurant> restaurantsList, List<MapMarker> mapMarkersList) {
        this.location = location;
        this.restaurantsList = restaurantsList;
        this.mapMarkersList = mapMarkersList;
    }


    @NonNull
    public Location getLocation() {
        return location;
    }

    @Nullable
    public List<Restaurant> getRestaurantsList() {
        return restaurantsList;
    }

    public List<MapMarker> getMapMarkersList() {
        return mapMarkersList;
    }

}
