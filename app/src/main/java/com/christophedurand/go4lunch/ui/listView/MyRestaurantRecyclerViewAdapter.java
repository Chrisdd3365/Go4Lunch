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

import com.christophedurand.go4lunch.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.Calendar;
import java.util.Date;
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

        //holder.mRestaurantImageView.setImageBitmap(restaurant.getPlace().getImage());
        holder.mRestaurantNameTextView.setText(restaurant.getPlace().getName());
        holder.mRestaurantAddressTextView.setText(restaurant.getPlace().getAddress());
        if (restaurant.getPlace().getOpeningHours() != null) {
            String placeOpeningHours = restaurant.getPlace().getOpeningHours().getWeekdayText().get(getDayOfWeek());
            holder.mRestaurantIsOpenTextView.setText(placeOpeningHours);
        }
        holder.mRestaurantRatingTextView.setText(String.valueOf(restaurant.getPlace().getRating()));
        holder.mRestaurantDistanceTextView.setText(getDistanceFromLastKnownLocation(restaurant));

        holder.itemView.setOnClickListener((v -> {
            mListener.onClickRestaurant(restaurant);
        }));
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    private int getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = (dayOfWeek == 1)? 6: dayOfWeek-2;
        return dayOfWeek;
    }

    private String getDistanceFromLastKnownLocation(PlaceLikelihood restaurant) {
        Location currentLocation = new Location("user current location");
        currentLocation.setLatitude(currentLocation.getLatitude());
        currentLocation.setLongitude(currentLocation.getLongitude());

        Location restaurantLocation = new Location("restaurant location");
        restaurantLocation.setLatitude(restaurant.getPlace().getLatLng().latitude);
        restaurantLocation.setLongitude(restaurant.getPlace().getLatLng().longitude);

        float distance = currentLocation.distanceTo(restaurantLocation);

        return (int)distance + "m";
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mRestaurantImageView;
        public final TextView mRestaurantNameTextView;
        public final TextView mRestaurantAddressTextView;
        public final TextView mRestaurantIsOpenTextView;
        public final TextView mRestaurantDistanceTextView;
        public final TextView mRestaurantRatingTextView;

        public ViewHolder(View view) {
            super(view);

            mRestaurantImageView = view.findViewById(R.id.restaurant_image_view);
            mRestaurantNameTextView = view.findViewById(R.id.restaurant_name);
            mRestaurantAddressTextView = view.findViewById(R.id.restaurant_address);
            mRestaurantIsOpenTextView = view.findViewById(R.id.restaurant_is_open);
            mRestaurantDistanceTextView = view.findViewById(R.id.restaurant_distance);
            mRestaurantRatingTextView = view.findViewById(R.id.restaurant_rating);
        }
    }
}