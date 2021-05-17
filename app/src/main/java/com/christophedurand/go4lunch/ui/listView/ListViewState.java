package com.christophedurand.go4lunch.ui.listView;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;

import java.util.List;
import java.util.Map;


class ListViewState {

    @NonNull
    private final Location location;
    @Nullable
    // TODO !!! Ca doit être un ViewState
    private final List<Restaurant> restaurantsList;
    @Nullable
    private final Map<String, RestaurantDetailsResponse> map;


    public ListViewState(@NonNull Location location,
                         @Nullable List<Restaurant> restaurantsList,
                         @Nullable Map<String, RestaurantDetailsResponse> map) {
        this.location = location;
        this.restaurantsList = restaurantsList;
        this.map = map;
    }


    @NonNull
    public Location getLocation() {
        return location;
    }

    @Nullable
    public List<Restaurant> getRestaurantsList() {
        return restaurantsList;
    }

    @Nullable
    public Map<String, RestaurantDetailsResponse> getMap() {
        return map;
    }


    @Override
    public String toString() {
        return "ListViewState{" +
                "location=" + location +
                ", restaurantsList=" + restaurantsList +
                ", map=" + map +
                '}';
    }

}
