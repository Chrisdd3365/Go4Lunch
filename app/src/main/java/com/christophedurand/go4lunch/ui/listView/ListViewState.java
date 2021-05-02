package com.christophedurand.go4lunch.ui.listView;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.christophedurand.go4lunch.model.pojo.Restaurant;

import java.util.List;


class ListViewState {

    @NonNull
    private final Location location;
    @Nullable
    private final List<Restaurant> restaurantsList;


    public ListViewState(@NonNull Location location, @Nullable List<Restaurant> restaurantsList) {
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


    @Override
    public String toString() {
        return "ListViewState{" +
                "location=" + location +
                ", restaurantsList=" + restaurantsList +
                '}';
    }

}
