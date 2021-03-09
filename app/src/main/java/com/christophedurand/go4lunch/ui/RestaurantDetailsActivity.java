package com.christophedurand.go4lunch.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.christophedurand.go4lunch.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import com.christophedurand.go4lunch.model.pojo.Restaurant;

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
        if (extras != null) {
            Restaurant restaurant = extras.getParcelable(BUNDLE_EXTRA_RESTAURANT);
            String id = restaurant.placeId;
            String name = restaurant.name;
            String address = restaurant.formattedAddress;

            // INIT UI IN VIEW LIFE CYCLE
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detailed_restaurant);

            // INIT && SET ALL ELEMENTS OF UI
            restaurantImageView = findViewById(R.id.restaurant_image_view);
            //setBitmap(id, placesClient, restaurantImageView);

            restaurantNameTextView = findViewById(R.id.restaurant_name_text_view);
            restaurantNameTextView.setText(name);

            restaurantAddressTextView = findViewById(R.id.restaurant_address_text_view);
            restaurantAddressTextView.setText(address);

            // SET ON CLICK LISTENER
            callImageButton = findViewById(R.id.call_image_button);
            callImageButton.setOnClickListener(v -> {
                //setPhoneNumber(id, placesClient);
            });

            likeImageButton = findViewById(R.id.like_image_button);
            likeImageButton.setOnClickListener(v -> {
                //like restaurant
            });

            websiteImageButton = findViewById(R.id.website_image_button);
            websiteImageButton.setOnClickListener(v -> {
                //setWebsite(id, placesClient);
            });
        }
    }


}
