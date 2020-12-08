package com.christophedurand.go4lunch.ui.mapView;


import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.christophedurand.go4lunch.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import model.RestaurantDataAsyncTask;


public class RestaurantInfoAdapter implements GoogleMap.InfoWindowAdapter {

    //-- PROPERTIES
    private final View mRestaurantInfoView;
    private String placeId;
    private final PlacesClient placesClient;
    private final Place place;


    //-- CONSTRUCTOR
    public RestaurantInfoAdapter(View mRestaurantInfoView, String placeId, PlacesClient placesClient, Place place) {
        this.mRestaurantInfoView = mRestaurantInfoView;
        this.placeId = placeId;
        this.placesClient = placesClient;
        this.place = place;
    }


    //-- METHODS
    @Override
    public View getInfoWindow(Marker marker) {
        // Define a Place ID.
        placeId = (String) marker.getTag();

        new RestaurantDataAsyncTask(marker, placeId, placesClient).execute();

        TextView nameTextView = mRestaurantInfoView.findViewById(R.id.name_text_view);
        nameTextView.setText(marker.getTitle());

        TextView descriptionTextView = mRestaurantInfoView.findViewById(R.id.address_text_view);
        descriptionTextView.setText(marker.getSnippet());

        ImageView restaurantImageView = mRestaurantInfoView.findViewById(R.id.restaurant_image_view);

        // Specify the fields to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        // Construct a request object, passing the place ID and fields array.
        assert placeId != null;
        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(Objects.requireNonNull(placeId), fields).build();

        // FETCH RESTAURANT DETAILS BY ID
        // Add a listener to handle the response.
        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {

            Place place = response.getPlace();
            // Get the photo metadata.
            PhotoMetadata photoMetadata = Objects.requireNonNull(place.getPhotoMetadatas()).get(0);

            // Create a FetchPhotoRequest.
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build();

            // FETCH RESTAURANT PHOTO
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
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

        return mRestaurantInfoView;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }




}
