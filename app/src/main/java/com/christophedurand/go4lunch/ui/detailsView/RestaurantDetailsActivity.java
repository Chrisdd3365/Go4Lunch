package com.christophedurand.go4lunch.ui.detailsView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.ViewModelFactory;
import com.christophedurand.go4lunch.ui.listView.ListInterface;
import com.christophedurand.go4lunch.ui.workmatesView.WorkmatesRecyclerViewAdapter;
import com.christophedurand.go4lunch.utils.Utils;


public class RestaurantDetailsActivity extends AppCompatActivity implements ListInterface {

    private ImageButton likeImageButton;
    private ImageButton joiningImageButton;

    private RecyclerView workmatesRecyclerView;
    private WorkmatesRecyclerViewAdapter workmatesRecyclerViewAdapter;

    private String restaurantId;
    private RestaurantDetailsViewModel restaurantDetailsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailed_restaurant);

        joiningImageButton = findViewById(R.id.joining_image_button);
        likeImageButton = findViewById(R.id.like_image_button);

        configureWorkmatesRecyclerView();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            restaurantId = extras.getString("restaurantId");
        }

        configureViewModel();
        subscribe();

        configureButtonState();

        likeButtonIsTapped();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        restaurantDetailsViewModel.getDetailsViewStateLiveData().removeObservers(this);
    }


    @Override
    public void onClickItemList(String restaurantId) { }

    private void joiningButtonIsTapped(String chosenRestaurantName, String chosenRestaurantAddress) {
        joiningImageButton.setOnClickListener(view -> {
            restaurantDetailsViewModel.setJoiningButtonState(chosenRestaurantName, chosenRestaurantAddress);
        });
    }

    private void callButtonIsTapped(String phoneNumber) {
        ImageButton callButton = findViewById(R.id.call_image_button);
        callButton.setOnClickListener(view -> {
            if (phoneNumber != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            } else {
                Toast.makeText(this, getString(R.string.no_phone_number_title), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void likeButtonIsTapped() {
        likeImageButton.setOnClickListener(view -> {
            restaurantDetailsViewModel.setFavoriteButtonState();
        });
    }

    private void websiteButtonIsTapped(String websiteURL) {
        ImageButton websiteButton = findViewById(R.id.website_image_button);
        websiteButton.setOnClickListener(view -> {
            if (websiteURL != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, getString(R.string.no_website_title), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void configureViewModel() {
        ViewModelFactory viewModelFactory = new ViewModelFactory(restaurantId);
        restaurantDetailsViewModel = new ViewModelProvider(this, viewModelFactory).get(RestaurantDetailsViewModel.class);
    }

    private void subscribe() {
        restaurantDetailsViewModel.getDetailsViewStateLiveData().observe(this, detailsViewState -> {
            String photoReference = detailsViewState.getPhotoURL();
            configureRestaurantImage(photoReference);

            String restaurantName = detailsViewState.getRestaurantName();
            configureRestaurantName(restaurantName);

            String restaurantAddress = detailsViewState.getRestaurantAddress();
            configureRestaurantAddress(restaurantAddress);

            String phoneNumber = detailsViewState.getPhoneNumber();
            callButtonIsTapped(phoneNumber);

            String websiteURL = detailsViewState.getWebsiteURL();
            websiteButtonIsTapped(websiteURL);

            int joiningDrawableResId = detailsViewState.getJoiningButtonDrawableResId();
            joiningImageButton.setImageResource(joiningDrawableResId);

            int favoriteDrawableResId = detailsViewState.getFavoriteButtonDrawableResId();
            likeImageButton.setImageResource(favoriteDrawableResId);

            workmatesRecyclerViewAdapter = new WorkmatesRecyclerViewAdapter(this);
            workmatesRecyclerViewAdapter.setNewData(detailsViewState.getWorkmatesList());
            workmatesRecyclerView.setAdapter(workmatesRecyclerViewAdapter);

            joiningButtonIsTapped(restaurantName, restaurantAddress);
        });
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

    private void configureButtonState() {
        restaurantDetailsViewModel.getJoiningButtonState();
        restaurantDetailsViewModel.getFavoriteButtonState();
    }

    private void configureWorkmatesRecyclerView() {
        workmatesRecyclerView = findViewById(R.id.workmates_list_recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        workmatesRecyclerView.setLayoutManager(layoutManager);
        workmatesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        workmatesRecyclerView.setClickable(true);
    }

}
