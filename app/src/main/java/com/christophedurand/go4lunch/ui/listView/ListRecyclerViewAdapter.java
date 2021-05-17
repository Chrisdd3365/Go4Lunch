package com.christophedurand.go4lunch.ui.listView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophedurand.go4lunch.R;

import java.util.List;
import java.util.Map;

import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetails;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;


public class ListRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private final Activity activity;
    private final Location currentLocation;
    private final List<Restaurant> dataSet;
    private final Map<String, RestaurantDetailsResponse> map;


    public ListRecyclerViewAdapter(Activity activity,
                                   Location currentLocation,
                                   List<Restaurant> dataSet,
                                   Map<String, RestaurantDetailsResponse> map) {
        this.activity = activity;
        this.currentLocation = currentLocation;
        this.dataSet = dataSet;
        this.map = map;
    }


    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = dataSet.get(position);
        String restaurantId = restaurant.getPlaceId();
        RestaurantDetailsResponse restaurantDetailsResponse = map.get(restaurantId);
        holder.bind(activity, currentLocation, restaurant, restaurantDetailsResponse);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}