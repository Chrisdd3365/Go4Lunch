package com.christophedurand.go4lunch.ui.detailsView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.utils.ImageUtils;


public class RestaurantDetailsActivity extends AppCompatActivity {

    public static final String RESTAURANT_ID = "RESTAURANT_ID";

    private ImageView restaurantImageView;
    private TextView restaurantNameTextView;
    private TextView restaurantAddressTextView;
    private ImageButton callImageButton;
    private ImageButton likeImageButton;
    private ImageButton websiteImageButton;

    private String restaurantId;
    private RestaurantDetailsViewModel restaurantDetailsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            restaurantId = extras.getString(RESTAURANT_ID);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detailed_restaurant);

            restaurantImageView = findViewById(R.id.restaurant_image_view);
            restaurantNameTextView = findViewById(R.id.restaurant_name_text_view);
            restaurantAddressTextView = findViewById(R.id.restaurant_address_text_view);
            callImageButton = findViewById(R.id.call_image_button);
            likeImageButton = findViewById(R.id.like_image_button);
            websiteImageButton = findViewById(R.id.website_image_button);

            configureViewModel();

            restaurantDetailsViewModel.getDetailsUiModelLiveData().observe(this, detailsUiModel -> {

                String photoReference = detailsUiModel.getRestaurantDetails().photos.get(0).getPhotoReference();
                ImageUtils.loadGooglePhoto(this, restaurantImageView, photoReference);

                String name = detailsUiModel.getRestaurantDetails().name;
                restaurantNameTextView.setText(name);

                String formattedAddress = detailsUiModel.getRestaurantDetails().formattedAddress;
                restaurantAddressTextView.setText(formattedAddress);

                //TODO: refactoring into VIEW MODEL
                if (detailsUiModel.getRestaurantDetails().internationalPhoneNumber != null) {
                    callImageButton.setOnClickListener(v -> {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(
                                Uri.parse(
                                        "tel:" + detailsUiModel.getRestaurantDetails().getInternationalPhoneNumber()
                                )
                        );
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Numéro de téléphone indisponible", Toast.LENGTH_SHORT).show();
                }

                //TODO: create live data to manage favorite
                likeImageButton.setOnClickListener(v -> {
                    //like restaurant
                });

                //TODO: refactoring into VIEW MODEL
                if (detailsUiModel.getRestaurantDetails().website != null) {
                    websiteImageButton.setOnClickListener(v -> {
                        Uri websiteUri = Uri.parse(detailsUiModel.getRestaurantDetails().website);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, websiteUri);
                        startActivity(launchBrowser);
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Site internet indisponible", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    public static void startActivity(Activity activity, String restaurantId) {
        activity.startActivity(
                new Intent(
                        activity,
                        RestaurantDetailsActivity.class
                )
                        .putExtra(RESTAURANT_ID, restaurantId)
        );
    }

    private void configureViewModel() {
        RestaurantDetailsViewModelFactory restaurantDetailsViewModelFactory = new RestaurantDetailsViewModelFactory(restaurantId);
        restaurantDetailsViewModel = new ViewModelProvider(
                this,
                restaurantDetailsViewModelFactory).get(RestaurantDetailsViewModel.class);
    }

}
