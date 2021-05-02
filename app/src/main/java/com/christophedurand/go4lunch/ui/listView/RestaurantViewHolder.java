package com.christophedurand.go4lunch.ui.listView;


import android.app.Activity;
import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsActivity;
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


    public void bind(Activity activity, Location currentLocation, Restaurant restaurant) {

        String photoReference = restaurant.getPhotos().get(0).getPhotoReference();
        ImageUtils.loadGooglePhoto(itemView.getContext(), mRestaurantImageView, photoReference);

        String name = restaurant.name;
        mRestaurantNameTextView.setText(name);

        String vicinity = restaurant.vicinity;
        mRestaurantAddressTextView.setText(vicinity);

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

//        com.christophedurand.go4lunch.model.pojo.Location restaurantLocation = restaurant.geometry.location;
//        String distanceFromUser = ListViewModel.getDistanceFromLastKnownUserLocation(currentLocation, restaurantLocation);
//        mRestaurantDistanceTextView.setText(distanceFromUser);

        double rating = restaurant.rating;
        mRestaurantRatingTextView.setText(String.valueOf(rating));

        itemView.setOnClickListener(v -> {
            RestaurantDetailsActivity.startActivity(
                    activity,
                    restaurant.placeId
            );
        });
    }

}
