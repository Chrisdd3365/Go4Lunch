package com.christophedurand.go4lunch.ui.listView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.christophedurand.go4lunch.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Restaurant}.
 */
public class MyRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<MyRestaurantRecyclerViewAdapter.ViewHolder> {

    //-- PROPERTIES
    private final List<PlaceLikelihood> mRestaurants;
    private final ListRestaurantsInterface mListener;
    private final PlacesClient mPlacesClient;
    private final Location mCurrentUserLocation;

    //-- CONSTRUCTOR
    public MyRestaurantRecyclerViewAdapter(List<PlaceLikelihood> items,
                                           ListRestaurantsInterface listener,
                                           PlacesClient placesClient,
                                           Location currentUserLocation) {
        mRestaurants = items;
        mListener = listener;
        mPlacesClient = placesClient;
        mCurrentUserLocation = currentUserLocation;
    }


    //-- VIEW HOLDER
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


    //-- VIEW HOLDER LIFE CYCLE
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        PlaceLikelihood restaurant = mRestaurants.get(position);

        holder.mRestaurantNameTextView.setText(restaurant.getPlace().getName());
        holder.mRestaurantAddressTextView.setText(restaurant.getPlace().getAddress());
        holder.mRestaurantRatingTextView.setText(String.valueOf(restaurant.getPlace().getRating()));
        holder.mRestaurantDistanceTextView.setText(getDistanceFromLastKnownUserLocation(mCurrentUserLocation, position));

        setRestaurantIsOpen(restaurant.getPlace().getId(), holder.mRestaurantIsOpenTextView, position);
        setBitmapRestaurant(restaurant.getPlace().getId(), holder.mRestaurantImageView);

        holder.itemView.setOnClickListener((v -> {
            mListener.onClickRestaurant(restaurant);
            Log.d("RESTAURANT ON CLICK", "restaurant is : " + restaurant);
        }));
    }


    //-- METHODS
    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    private String getDistanceFromLastKnownUserLocation(Location currentUserLocation, int position) {
        Location restaurantLocation = new Location("restaurant location");
        restaurantLocation.setLatitude(Objects.requireNonNull(mRestaurants.get(position).getPlace().getLatLng()).latitude);
        restaurantLocation.setLongitude(Objects.requireNonNull(mRestaurants.get(position).getPlace().getLatLng()).longitude);

        float distance = currentUserLocation.distanceTo(restaurantLocation);
        Log.d("DISTANCE", "distance is : " + distance);

        return (int)distance + "m";
    }

    private void setRestaurantIsOpen(String placeId, TextView isOpenTextView, int position) {
        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.OPENING_HOURS);

        // Construct a request object, passing the place ID and fields array.
        assert placeId != null;
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        // Add a listener to handle the response.
        mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            isOpenTextView.setText(String.valueOf(Objects.requireNonNull(
                    place.getOpeningHours()).getPeriods().get(position).getOpen())
            );
            Log.d("OPEN NOW ", "open now is : " + place.getOpeningHours().getPeriods().get(position).getOpen());

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                // Handle error with given status code.
                Log.e("TAG", "Place not found: " + exception.getMessage());
            }
        });

    }

    private void setBitmapRestaurant(String placeId, ImageView restaurantImageView) {
        // Specify the fields to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        // Construct a request object, passing the place ID and fields array.
        assert placeId != null;
        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(Objects.requireNonNull(placeId), fields).build();

        // FETCH RESTAURANT DETAILS BY ID
        // Add a listener to handle the response.
        mPlacesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {

            Place place = response.getPlace();
            // Get the photo metadata.
            PhotoMetadata photoMetadata = Objects.requireNonNull(place.getPhotoMetadatas()).get(0);

            // Create a FetchPhotoRequest.
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build();

            // FETCH RESTAURANT PHOTO
            mPlacesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                restaurantImageView.setImageBitmap(bitmap);

            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    Log.e("TAG", "Photo not found: " + exception.getMessage());
                }
            });

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                Log.e("TAG", "Place not found: " + exception.getMessage());
            }
        });
    }

}