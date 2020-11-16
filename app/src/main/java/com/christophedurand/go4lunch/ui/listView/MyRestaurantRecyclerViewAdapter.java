package com.christophedurand.go4lunch.ui.listView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.christophedurand.go4lunch.R;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Restaurant}.
 */
public class MyRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<MyRestaurantRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceLikelihood> mRestaurants;
    private final ListRestaurantsInterface mListener;


    public MyRestaurantRecyclerViewAdapter(List<PlaceLikelihood> items, ListRestaurantsInterface listener) {
        mRestaurants = items;
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d("MyRestaurantRVA", "onBindViewHolder called");

        PlaceLikelihood restaurant = mRestaurants.get(position);

        holder.mRestaurantNameTextView.setText(restaurant.getPlace().getName());
        holder.mRestaurantAddressTextView.setText(restaurant.getPlace().getAddress());
        holder.mRestaurantBusinessStatus.setText(String.valueOf(restaurant.getPlace().getBusinessStatus()));

        holder.itemView.setOnClickListener((v -> {
            mListener.onClickRestaurant(restaurant);
        }));
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mRestaurantNameTextView;
        public final TextView mRestaurantAddressTextView;
        public final TextView mRestaurantBusinessStatus;

        public ViewHolder(View view) {
            super(view);

            mRestaurantNameTextView = view.findViewById(R.id.restaurant_name);
            mRestaurantAddressTextView = view.findViewById(R.id.restaurant_address);
            mRestaurantBusinessStatus = view.findViewById(R.id.restaurant_business_status);
        }
    }
}