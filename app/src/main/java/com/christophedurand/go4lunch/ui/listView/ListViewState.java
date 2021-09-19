package com.christophedurand.go4lunch.ui.listView;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;


public class ListViewState {

    @NonNull
    private final Location location;
    @Nullable
    private final List<RestaurantViewState> restaurantViewStatesList;
    @Nullable
    private final String searchQuery;


    public ListViewState(@NonNull Location location, @Nullable List<RestaurantViewState> restaurantViewStatesList, @Nullable String searchQuery) {
        this.location = location;
        this.restaurantViewStatesList = restaurantViewStatesList;
        this.searchQuery = searchQuery;
    }


    @NonNull
    public Location getLocation() {
        return location;
    }

    @Nullable
    public List<RestaurantViewState> getRestaurantViewStatesList() {
        return restaurantViewStatesList;
    }

    @Nullable
    public String getSearchQuery() {
        return searchQuery;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListViewState that = (ListViewState) o;
        return location.equals(that.location) && Objects.equals(restaurantViewStatesList, that.restaurantViewStatesList) && Objects.equals(searchQuery, that.searchQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, restaurantViewStatesList, searchQuery);
    }

    @Override
    public String toString() {
        return "ListViewState{" +
                "location=" + location +
                ", restaurantViewStatesList=" + restaurantViewStatesList +
                ", searchQuery='" + searchQuery + '\'' +
                '}';
    }

}
