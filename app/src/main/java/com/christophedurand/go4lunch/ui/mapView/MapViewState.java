package com.christophedurand.go4lunch.ui.mapView;


import android.location.Location;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;


public class MapViewState {

    @NonNull
    private final Location location;
    private final List<MapMarker> mapMarkersList;
    private final String searchQuery;


    public MapViewState(@NonNull Location location, List<MapMarker> mapMarkersList, String searchQuery) {
        this.location = location;
        this.mapMarkersList = mapMarkersList;
        this.searchQuery = searchQuery;
    }


    @NonNull
    public Location getLocation() {
        return location;
    }

    public List<MapMarker> getMapMarkersList() {
        return mapMarkersList;
    }

    public String getSearchQuery() {
        return searchQuery;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapViewState that = (MapViewState) o;
        return location.equals(that.location) && Objects.equals(mapMarkersList, that.mapMarkersList) && Objects.equals(searchQuery, that.searchQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, mapMarkersList, searchQuery);
    }

    @Override
    public String toString() {
        return "MapViewState{" +
                "location=" + location +
                ", mapMarkersList=" + mapMarkersList +
                ", searchQuery='" + searchQuery + '\'' +
                '}';
    }

}
