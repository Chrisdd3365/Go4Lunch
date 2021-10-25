package com.christophedurand.go4lunch.ui.listView;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class ListViewState {

    @NonNull
    private final Location location;
    @Nullable
    private final List<RestaurantViewState> restaurantViewStatesList;


    public ListViewState(@NonNull Location location, @Nullable List<RestaurantViewState> restaurantViewStatesList) {
        this.location = location;
        this.restaurantViewStatesList = restaurantViewStatesList;
    }


    @NonNull
    public Location getLocation() {
        return location;
    }

    @Nullable
    public List<RestaurantViewState> getRestaurantViewStatesList() {
        return restaurantViewStatesList;
    }

}
