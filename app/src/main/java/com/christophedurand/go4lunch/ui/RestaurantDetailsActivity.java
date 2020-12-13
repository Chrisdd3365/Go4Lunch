package com.christophedurand.go4lunch.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.christophedurand.go4lunch.R;
import com.facebook.internal.LockOnGetVariable;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RestaurantDetailsActivity extends AppCompatActivity {

    //-- PROPERTIES
    // STATIC
    public static final String BUNDLE_EXTRA_RESTAURANT = "BUNDLE_EXTRA_RESTAURANT";

    // GOOGLE MAPS SDK
    private PlacesClient placesClient;

    // UI
    private ImageView restaurantImageView;
    private TextView restaurantNameTextView;
    private TextView restaurantAddressTextView;
    private ImageButton callImageButton;
    private ImageButton likeImageButton;
    private ImageButton websiteImageButton;


    //-- VIEW LIFE CYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create a new PlacesClient instance
        placesClient = Places.createClient(this);

        Bundle extras = getIntent().getExtras();
        Log.d("EXTRAS BUNDLE", "extras bundle is : " + extras);

        if (extras != null) {
            // RESTAURANT
            PlaceLikelihood restaurant = extras.getParcelable(BUNDLE_EXTRA_RESTAURANT);
            Log.d("RESTAURANT", "restaurant is : " + restaurant);
            // RESTAURANTS DATA
            String id = restaurant.getPlace().getId();
            Log.d("RESTAURANT ID", "restaurant id is : " + id);
            String name = restaurant.getPlace().getName();
            String address = restaurant.getPlace().getAddress();

            // INIT UI IN VIEW LIFE CYCLE
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detailed_restaurant);

            // INIT && SET ALL ELEMENTS OF UI
            restaurantImageView = findViewById(R.id.restaurant_image_view);
            setBitmap(id, placesClient, restaurantImageView);

            restaurantNameTextView = findViewById(R.id.restaurant_name_text_view);
            restaurantNameTextView.setText(name);

            restaurantAddressTextView = findViewById(R.id.restaurant_address_text_view);
            restaurantAddressTextView.setText(address);

            callImageButton = findViewById(R.id.call_image_button);
            callImageButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_CALL);
                setPhoneNumber(id, placesClient, intent);
                startActivity(intent);
            });

            likeImageButton = findViewById(R.id.like_image_button);
            likeImageButton.setOnClickListener(v -> {
                //like restaurant
            });

            websiteImageButton = findViewById(R.id.website_image_button);
            websiteImageButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                setWebsite(id, placesClient, intent);
                startActivity(intent);
            });
        }
    }


    //-- METHODS
    private void setPhoneNumber(String placeId,
                                             PlacesClient placesClient,
                                             Intent intentPhoneNumber) {
        // Specify the fields to return.
        List<Place.Field> placeFieldsById = Arrays.asList(Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI);

        // Construct a request object, passing the place ID and fields array.
        assert placeId != null;
        FetchPlaceRequest requestId = FetchPlaceRequest.builder(Objects.requireNonNull(placeId), placeFieldsById).build();

        // FETCH RESTAURANT DETAILS BY ID
        // Add a listener to handle the response.
        placesClient.fetchPlace(requestId).addOnSuccessListener((response) -> {

            Place place = response.getPlace();
            intentPhoneNumber.setData(Uri.parse(place.getPhoneNumber()));

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                Log.e("TAG", "Place not found: " + exception.getMessage());
            }
        });
    }

    private void setWebsite(String placeId,
                                PlacesClient placesClient,
                                Intent intentWebsite) {
        // Specify the fields to return.
        List<Place.Field> placeFieldsById = Arrays.asList(Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI);

        // Construct a request object, passing the place ID and fields array.
        assert placeId != null;
        FetchPlaceRequest requestId = FetchPlaceRequest.builder(Objects.requireNonNull(placeId), placeFieldsById).build();

        // FETCH RESTAURANT DETAILS BY ID
        // Add a listener to handle the response.
        placesClient.fetchPlace(requestId).addOnSuccessListener((response) -> {

            Place place = response.getPlace();
            intentWebsite.setData(place.getWebsiteUri());

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                Log.e("TAG", "Place not found: " + exception.getMessage());
            }
        });
    }


    private void setBitmap(String placeId, PlacesClient placesClient, ImageView restaurantImageView) {
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
    }
}
