package com.christophedurand.go4lunch.ui.listView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.christophedurand.go4lunch.R;

import java.util.List;

import model.pojo.Restaurant;

import com.christophedurand.go4lunch.ui.mapView.MapViewModel;


public class RestaurantsListRecyclerViewAdapter
        extends RecyclerView.Adapter<RestaurantsListRecyclerViewAdapter.RestaurantsItemViewHolder> {

    private final List<Restaurant> mRestaurants;
    private final RestaurantsListInterface mListener;
    private final MapViewModel mViewModel;
    private final Location mLocation;

    public RestaurantsListRecyclerViewAdapter(List<Restaurant> items,
                                              RestaurantsListInterface listener,
                                              MapViewModel viewModel,
                                              Location location) {
        mRestaurants = items;
        mListener = listener;
        mViewModel = viewModel;
        mLocation = location;
    }


    @NonNull
    @Override
    public RestaurantsListRecyclerViewAdapter.RestaurantsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent, false);
        return new RestaurantsListRecyclerViewAdapter.RestaurantsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsListRecyclerViewAdapter.RestaurantsItemViewHolder holder, int position) {
        holder.bind(position, mListener);
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    class RestaurantsItemViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mRestaurantImageView;
        public final TextView mRestaurantNameTextView;
        public final TextView mRestaurantAddressTextView;
        public final TextView mRestaurantIsOpenTextView;
        public final TextView mRestaurantDistanceTextView;
        public final TextView mRestaurantRatingTextView;

        public RestaurantsItemViewHolder(View view) {
            super(view);

            mRestaurantImageView = view.findViewById(R.id.restaurant_image_view);
            mRestaurantNameTextView = view.findViewById(R.id.restaurant_name);
            mRestaurantAddressTextView = view.findViewById(R.id.restaurant_address);
            mRestaurantIsOpenTextView = view.findViewById(R.id.restaurant_is_open);
            mRestaurantDistanceTextView = view.findViewById(R.id.restaurant_distance);
            mRestaurantRatingTextView = view.findViewById(R.id.restaurant_rating);
        }

        public void bind(int position, RestaurantsListInterface mListener) {
            Restaurant restaurant = mRestaurants.get(position);

            Glide.with(itemView.getContext()).load(restaurant.icon).into(mRestaurantImageView);

            String name = restaurant.name;
            mRestaurantNameTextView.setText(name);

            String address = restaurant.formattedAddress;
            mRestaurantAddressTextView.setText(address);

//            boolean isOpen = restaurant.openingHours.isOpenNow();
//            if (isOpen) {
//                String openString = "Ouvert";
//                mRestaurantIsOpenTextView.setText(openString);
//                mRestaurantIsOpenTextView.setTextColor(itemView.getResources().getColor(R.color.colorRed));
//            } else {
//                String closeString = "Fermé";
//                mRestaurantIsOpenTextView.setText(closeString);
//                mRestaurantIsOpenTextView.setTextColor(itemView.getResources().getColor(R.color.colorGreen));
//            }

//            String distanceFromUser = mViewModel.getDistanceFromLastKnownUserLocation(position, mLocation);
//            mRestaurantDistanceTextView.setText(distanceFromUser);

            double rating = restaurant.rating;
            mRestaurantRatingTextView.setText(String.valueOf(rating));

            itemView.setOnClickListener((v -> {
                mListener.onClickRestaurant(restaurant);
                Log.d("RESTAURANT ON CLICK", "restaurant is : " + restaurant);
            }));
        }
    }

}