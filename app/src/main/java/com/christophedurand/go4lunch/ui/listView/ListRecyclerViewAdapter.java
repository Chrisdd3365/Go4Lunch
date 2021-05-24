package com.christophedurand.go4lunch.ui.listView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophedurand.go4lunch.R;

import java.util.ArrayList;
import java.util.List;


public class ListRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private final List<RestaurantViewState> mRestaurantViewStates = new ArrayList<>();

    private final ListInterface listener;


    public ListRecyclerViewAdapter(ListInterface listener) {
        this.listener = listener;
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
        RestaurantViewState restaurantViewState = mRestaurantViewStates.get(position);
        holder.bind(restaurantViewState, listener);
    }

    @Override
    public int getItemCount() {
        return mRestaurantViewStates.size();
    }

    public void setNewData(List<RestaurantViewState> newList) {
        mRestaurantViewStates.clear();
        mRestaurantViewStates.addAll(newList);
        notifyDataSetChanged();
    }

}