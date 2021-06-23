package com.christophedurand.go4lunch.ui.mapView;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.christophedurand.go4lunch.model.pojo.Restaurant;


public class MapViewState {

    @NonNull
    private final Location location;
    private final List<MapMarker> mapMarkersList;


    public MapViewState(@NonNull Location location, List<MapMarker> mapMarkersList) {
        this.location = location;
        this.mapMarkersList = mapMarkersList;
    }


    @NonNull
    public Location getLocation() {
        return location;
    }

    public List<MapMarker> getMapMarkersList() {
        return mapMarkersList;
    }

}
