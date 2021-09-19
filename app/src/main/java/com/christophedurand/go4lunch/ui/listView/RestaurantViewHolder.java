package com.christophedurand.go4lunch.ui.listView;


import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.workmatesView.User;
import com.christophedurand.go4lunch.ui.workmatesView.UserManager;
import com.christophedurand.go4lunch.utils.Utils;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;


class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mRestaurantImageView;
    private final TextView mRestaurantNameTextView;
    private final TextView mRestaurantAddressTextView;
    private final TextView mRestaurantIsOpenTextView;
    private final TextView mRestaurantDistanceTextView;
    private final TextView mRestaurantWorkmatesTextView;
    private final RatingBar mRestaurantRatingBar;


    public RestaurantViewHolder(View view) {
        super(view);

        mRestaurantImageView = view.findViewById(R.id.restaurant_image_view);
        mRestaurantNameTextView = view.findViewById(R.id.restaurant_name);
        mRestaurantAddressTextView = view.findViewById(R.id.restaurant_address);
        mRestaurantIsOpenTextView = view.findViewById(R.id.restaurant_opening_hours_text_view);
        mRestaurantDistanceTextView = view.findViewById(R.id.restaurant_distance_text_view);
        mRestaurantWorkmatesTextView = view.findViewById(R.id.workmates_text_view);
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

        UserManager.getInstance().getAllUsers().get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> workmatesList = queryDocumentSnapshots.getDocuments();
            int numberOfWorkmates = 0;
            for (DocumentSnapshot workmate : workmatesList) {
                User user = workmate.toObject(User.class);
                if (user.getRestaurant().getId() != null && user.getRestaurant().getId().equals(restaurantViewState.getPlaceId())) {
                    numberOfWorkmates += 1;
                    mRestaurantWorkmatesTextView.setText("(" + numberOfWorkmates + ")");
                }
            }
        });

        itemView.setOnClickListener(v -> {
            listener.onClickItemList(restaurantViewState.getPlaceId());
        });
    }

}
