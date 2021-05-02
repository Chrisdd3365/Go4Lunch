package com.christophedurand.go4lunch.ui.listView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophedurand.go4lunch.R;

import java.util.List;

import com.christophedurand.go4lunch.model.pojo.Restaurant;


public class ListRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private final Activity activity;
    private final Location currentLocation;
    private final List<Restaurant> dataSet;


    public ListRecyclerViewAdapter(Activity activity, Location currentLocation, List<Restaurant> dataSet) {
        this.activity = activity;
        this.currentLocation = currentLocation;
        this.dataSet = dataSet;
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
        holder.bind(activity, currentLocation, restaurant);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}