package com.christophedurand.go4lunch.ui.mapView;


import android.location.Location;

import androidx.annotation.NonNull;

import java.util.List;


public class MapViewState {

    @NonNull
    private final Location location;
    private final List<MapMarker> mapMarkersList;
    private final AutocompleteRestaurantViewState autocompleteRestaurantViewState;


    public MapViewState(@NonNull Location location, List<MapMarker> mapMarkersList, AutocompleteRestaurantViewState autocompleteRestaurantViewState) {
        this.location = location;
        this.mapMarkersList = mapMarkersList;
        this.autocompleteRestaurantViewState = autocompleteRestaurantViewState;
    }


    @NonNull
    public Location getLocation() {
        return location;
    }

    public List<MapMarker> getMapMarkersList() {
        return mapMarkersList;
    }

    public AutocompleteRestaurantViewState getAutocompleteRestaurantViewState() {
        return autocompleteRestaurantViewState;
    }

}
