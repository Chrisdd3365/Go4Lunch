package com.christophedurand.go4lunch.ui.listView;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.utils.ImageUtils;


class RestaurantViewHolder extends RecyclerView.ViewHolder {

    public final ImageView mRestaurantImageView;
    public final TextView mRestaurantNameTextView;
    public final TextView mRestaurantAddressTextView;
    public final TextView mRestaurantIsOpenTextView;
    public final TextView mRestaurantDistanceTextView;
    public final TextView mRestaurantRatingTextView;


    public RestaurantViewHolder(View view) {
        super(view);

        mRestaurantImageView = view.findViewById(R.id.item_restaurant_image_view);
        mRestaurantNameTextView = view.findViewById(R.id.restaurant_name);
        mRestaurantAddressTextView = view.findViewById(R.id.restaurant_address);
        mRestaurantIsOpenTextView = view.findViewById(R.id.restaurant_is_open);
        mRestaurantDistanceTextView = view.findViewById(R.id.restaurant_distance);
        mRestaurantRatingTextView = view.findViewById(R.id.restaurant_rating);

    }


    public void bind(RestaurantViewState restaurantViewState, ListInterface listener) {

        String photoURL = restaurantViewState.getPhotoURL();
        ImageUtils.loadGooglePhoto(itemView.getContext(), mRestaurantImageView, photoURL);

        String name = restaurantViewState.getName();
        mRestaurantNameTextView.setText(name);

        String address = restaurantViewState.getAddress();
        mRestaurantAddressTextView.setText(address);

        String openingHoursString = restaurantViewState.getOpeningHours();
        mRestaurantIsOpenTextView.setText(openingHoursString);


//        com.christophedurand.go4lunch.model.pojo.Location restaurantLocation = restaurant.geometry.location;
//        String distanceFromUser = ListViewModel.getDistanceFromLastKnownUserLocation(currentLocation, restaurantLocation);
//        mRestaurantDistanceTextView.setText(distanceFromUser);

        double rating = restaurantViewState.getRating();
        mRestaurantRatingTextView.setText(String.valueOf(rating));

        itemView.setOnClickListener(v -> {
            listener.onClickItemList(restaurantViewState.getPlaceId());
        });
    }

}
