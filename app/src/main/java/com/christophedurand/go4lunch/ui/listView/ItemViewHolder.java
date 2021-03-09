package com.christophedurand.go4lunch.ui.listView;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.christophedurand.go4lunch.R;

import java.util.List;

import com.christophedurand.go4lunch.model.pojo.Restaurant;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public final ImageView mRestaurantImageView;
    public final TextView mRestaurantNameTextView;
    public final TextView mRestaurantAddressTextView;
    public final TextView mRestaurantIsOpenTextView;
    public final TextView mRestaurantDistanceTextView;
    public final TextView mRestaurantRatingTextView;

    public ItemViewHolder(View view) {
        super(view);

        mRestaurantImageView = view.findViewById(R.id.restaurant_image_view);
        mRestaurantNameTextView = view.findViewById(R.id.restaurant_name);
        mRestaurantAddressTextView = view.findViewById(R.id.restaurant_address);
        mRestaurantIsOpenTextView = view.findViewById(R.id.restaurant_is_open);
        mRestaurantDistanceTextView = view.findViewById(R.id.restaurant_distance);
        mRestaurantRatingTextView = view.findViewById(R.id.restaurant_rating);
    }

    public void bind(List<Restaurant> restaurantsList, int position, ListInterface mListener, Location currentLocation) {
        Restaurant restaurant = restaurantsList.get(position);

        Glide.with(itemView.getContext()).load(restaurant.icon).into(mRestaurantImageView);

        String name = restaurant.name;
        mRestaurantNameTextView.setText(name);

        String address = restaurant.formattedAddress;
        mRestaurantAddressTextView.setText(address);

        boolean isOpen = restaurant.openingHours.openNow;
        if (isOpen) {
            String openString = "Ouvert";
            mRestaurantIsOpenTextView.setText(openString);
            mRestaurantIsOpenTextView.setTextColor(itemView.getResources().getColor(R.color.colorRed));
        } else {
            String closeString = "Fermé";
            mRestaurantIsOpenTextView.setText(closeString);
            mRestaurantIsOpenTextView.setTextColor(itemView.getResources().getColor(R.color.colorGreen));
        }

        String distanceFromUser = getDistanceFromLastKnownUserLocation(restaurantsList, position, currentLocation);
        mRestaurantDistanceTextView.setText(distanceFromUser);

        double rating = restaurant.rating;
        mRestaurantRatingTextView.setText(String.valueOf(rating));

        itemView.setOnClickListener((v -> {
            mListener.onClickRestaurant(restaurant);
            Log.d("RESTAURANT ON CLICK", "restaurant is : " + restaurant);
        }));
    }

    private String getDistanceFromLastKnownUserLocation(List<Restaurant> restaurantsList, int position, Location currentLocation) {
        Location restaurantLocation = new Location("restaurant location");
        restaurantLocation.setLatitude(restaurantsList.get(position).geometry.location.lat);
        restaurantLocation.setLongitude(restaurantsList.get(position).geometry.location.lng);

        float distance = currentLocation.distanceTo(restaurantLocation);
        Log.d("DISTANCE", "distance is : " + distance);

        return (int)distance + "m";
    }
}
