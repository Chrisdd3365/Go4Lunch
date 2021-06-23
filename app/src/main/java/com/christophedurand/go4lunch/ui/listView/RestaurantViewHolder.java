package com.christophedurand.go4lunch.ui.listView;


import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.utils.Utils;


class RestaurantViewHolder extends RecyclerView.ViewHolder {

    public final ImageView mRestaurantImageView;
    public final TextView mRestaurantNameTextView;
    public final TextView mRestaurantAddressTextView;
    public final TextView mRestaurantIsOpenTextView;
    public final TextView mRestaurantDistanceTextView;
    public final RatingBar mRestaurantRatingBar;


    public RestaurantViewHolder(View view) {
        super(view);

        mRestaurantImageView = view.findViewById(R.id.restaurant_image_view);
        mRestaurantNameTextView = view.findViewById(R.id.restaurant_name);
        mRestaurantAddressTextView = view.findViewById(R.id.restaurant_address);
        mRestaurantIsOpenTextView = view.findViewById(R.id.restaurant_opening_hours_text_view);
        mRestaurantDistanceTextView = view.findViewById(R.id.restaurant_distance_text_view);
        mRestaurantRatingBar = view.findViewById(R.id.restaurant_rating_bar);

    }


    public void bind(RestaurantViewState restaurantViewState, ListInterface listener) {

        String photoURL = restaurantViewState.getPhotoURL();
        Utils.loadGooglePhoto(itemView.getContext(), mRestaurantImageView, photoURL);

        String name = restaurantViewState.getName();
        mRestaurantNameTextView.setText(name);

        String address = restaurantViewState.getAddress();
        mRestaurantAddressTextView.setText(address);

        String openingHoursString = restaurantViewState.getOpeningHours();
        mRestaurantIsOpenTextView.setText(openingHoursString);

        String distanceString = restaurantViewState.getDistance();
        mRestaurantDistanceTextView.setText(distanceString);

        double rating = restaurantViewState.getRating();
        mRestaurantRatingBar.setRating((float) rating);

        itemView.setOnClickListener(v -> {
            listener.onClickItemList(restaurantViewState.getPlaceId());
        });
    }

}
