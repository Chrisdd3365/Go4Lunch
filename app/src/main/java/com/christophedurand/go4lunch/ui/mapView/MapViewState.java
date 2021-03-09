package com.christophedurand.go4lunch.ui.mapView;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.christophedurand.go4lunch.model.pojo.Restaurant;


class MapViewState {

    static class MapMarker {
        @NonNull
        private final String placeId;
        private final String name;
        private final String address;

        public MapMarker(@NonNull String placeId, String name, String address) {
            this.placeId = placeId;
            this.name = name;
            this.address = address;
        }

        @NonNull
        public String getPlaceId() {
            return placeId;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }
    }

    @NonNull
    private final Location location;
    @Nullable
    private final List<Restaurant> restaurantsList;
    @NonNull
    private final MapMarker mapMarker;


    public MapViewState(@NonNull Location location, @Nullable List<Restaurant> restaurantsList, @NonNull MapMarker mapMarker) {
        this.location = location;
        this.restaurantsList = restaurantsList;
        this.mapMarker = mapMarker;
    }


    @NonNull
    public Location getLocation() {
        return location;
    }

    @Nullable
    public List<Restaurant> getRestaurantsList() {
        return restaurantsList;
    }

    @NonNull
    public MapMarker getMapMarker() {
        return mapMarker;
    }

}
