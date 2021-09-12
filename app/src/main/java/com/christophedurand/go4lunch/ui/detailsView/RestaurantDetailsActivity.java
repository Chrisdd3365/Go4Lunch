package com.christophedurand.go4lunch.ui.detailsView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.model.UserManager;
import com.christophedurand.go4lunch.utils.Utils;

import java.util.ArrayList;


public class RestaurantDetailsActivity extends AppCompatActivity {

    private String restaurantId;
    private RestaurantDetailsViewModel restaurantDetailsViewModel;
    private final UserManager userManager = UserManager.getInstance();
    private ArrayList<String> favoriteRestaurantIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailed_restaurant);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            restaurantId = extras.getString("restaurantId");
        }

        configureViewModel();

        restaurantDetailsViewModel.getDetailsViewStateLiveData().observe(this, detailsViewState -> {
            String photoReference = detailsViewState.getRestaurantDetailsViewState().getPhotoURL();
            configureRestaurantImage(photoReference);

            String restaurantName = detailsViewState.getRestaurantDetailsViewState().getRestaurantName();
            configureRestaurantName(restaurantName);

            String restaurantAddress = detailsViewState.getRestaurantDetailsViewState().getRestaurantAddress();
            configureRestaurantAddress(restaurantAddress);

            String phoneNumber = detailsViewState.getRestaurantDetailsViewState().getPhoneNumber();
            callButtonIsTapped(phoneNumber);

            String websiteURL = detailsViewState.getRestaurantDetailsViewState().getWebsiteURL();
            websiteButtonIsTapped(websiteURL);

        });

        likeButtonIsTapped();

    }

    private void configureRestaurantImage(String photoReference) {
        ImageView restaurantImageView = findViewById(R.id.restaurant_image_view);
        Glide.with(this).load(Utils.buildGooglePhotoURL(photoReference)).into(restaurantImageView);
    }

    private void configureRestaurantName(String restaurantName) {
        TextView restaurantNameTextView = findViewById(R.id.restaurant_name_text_view);
        restaurantNameTextView.setText(restaurantName);
    }

    private void configureRestaurantAddress(String restaurantAddress) {
        TextView restaurantAddressTextView = findViewById(R.id.restaurant_address_text_view);
        restaurantAddressTextView.setText(restaurantAddress);
    }

    private ImageButton configureLikeButton() {
        ImageButton likeButton = findViewById(R.id.like_image_button);
        if (favoriteRestaurantIds.contains(restaurantId)) {
            likeButton.setImageResource(R.drawable.ic_star_filled);
        } else {
            likeButton.setImageResource(R.drawable.ic_star_outline);
        }
        return likeButton;
    }

    private void callButtonIsTapped(String phoneNumber) {
        ImageButton callButton = findViewById(R.id.call_image_button);
        callButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });
    }

    private void likeButtonIsTapped() {

        configureLikeButton().setOnClickListener(view -> {
            if (favoriteRestaurantIds.contains(restaurantId)) {
                favoriteRestaurantIds.remove(restaurantId);
            } else {
                favoriteRestaurantIds.add(restaurantId);
            }
        });
    }

    private void websiteButtonIsTapped(String websiteURL) {
        ImageButton websiteButton = findViewById(R.id.website_image_button);
        websiteButton.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
            startActivity(browserIntent);
        });
    }

    private void configureViewModel() {
        RestaurantDetailsViewModelFactory restaurantDetailsViewModelFactory = new RestaurantDetailsViewModelFactory(restaurantId);
        restaurantDetailsViewModel = new ViewModelProvider(this, restaurantDetailsViewModelFactory).get(RestaurantDetailsViewModel.class);
    }

}
