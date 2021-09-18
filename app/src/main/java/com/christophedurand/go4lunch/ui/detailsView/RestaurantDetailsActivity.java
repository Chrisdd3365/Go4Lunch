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
import com.christophedurand.go4lunch.model.UserManager;
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

        configureWorkmatesRecyclerView();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            restaurantId = extras.getString("restaurantId");
        }

        configureViewModel();
        subscribe();

        configureLikeButtonImage();
        likeButtonIsTapped();

        configureJoiningButtonImage();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (workmatesRecyclerViewAdapter != null) {
            workmatesRecyclerViewAdapter.startListening();
        }
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
        workmatesRecyclerViewAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        restaurantDetailsViewModel.onCleared();
    }


    @Override
    public void onClickItemList(String restaurantId) { }

    private void joiningButtonIsTapped(String chosenRestaurantName, String chosenRestaurantAddress) {
        joiningImageButton.setOnClickListener(view -> {
            UserManager.getInstance().getCurrentUserData().addOnSuccessListener(currentUser -> {
                if ((currentUser.getRestaurant() != null && currentUser.getRestaurant().getId() != null)
                        && currentUser.getRestaurant().getId().contains(restaurantId)) {
                    UserManager.getInstance().updateChosenRestaurant("", "", "", currentUser.getUid());
                    joiningImageButton.setImageResource(R.drawable.ic_check_circle_red);
                } else {
                    UserManager.getInstance().updateChosenRestaurant(restaurantId, chosenRestaurantName, chosenRestaurantAddress, currentUser.getUid());
                    joiningImageButton.setImageResource(R.drawable.ic_check_circle_green);
                }
            });
        });
    }

    private void callButtonIsTapped(String phoneNumber) {
        ImageButton callButton = findViewById(R.id.call_image_button);
        callButton.setOnClickListener(view -> {
            if (phoneNumber != null) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Pas de numéro de téléphone", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void likeButtonIsTapped() {
        likeImageButton.setOnClickListener(view -> {
            UserManager.getInstance().getCurrentUserData().addOnSuccessListener(currentUser -> {
                if (currentUser.getFavoriteRestaurantsIdsList() == null || !currentUser.getFavoriteRestaurantsIdsList().contains(restaurantId)) {
                    UserManager.getInstance().updateFavoriteRestaurantsIdsList(restaurantId, currentUser.getUid());
                    currentUser.getFavoriteRestaurantsIdsList().add(restaurantId);
                    likeImageButton.setImageResource(R.drawable.ic_star_filled);
                } else {
                    UserManager.getInstance().updateFavoriteRestaurantsIdsList(restaurantId, currentUser.getUid());
                    currentUser.getFavoriteRestaurantsIdsList().remove(restaurantId);
                    likeImageButton.setImageResource(R.drawable.ic_star_outline);
                }
            });
        });
    }

    private void websiteButtonIsTapped(String websiteURL) {
        ImageButton websiteButton = findViewById(R.id.website_image_button);
        websiteButton.setOnClickListener(view -> {
            if (websiteURL != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "Pas de site internet disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void configureViewModel() {
        RestaurantDetailsViewModelFactory restaurantDetailsViewModelFactory = new RestaurantDetailsViewModelFactory(restaurantId, this);
        restaurantDetailsViewModel = new ViewModelProvider(this, restaurantDetailsViewModelFactory).get(RestaurantDetailsViewModel.class);
    }

    private void subscribe() {
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

            workmatesRecyclerViewAdapter = new WorkmatesRecyclerViewAdapter(this, detailsViewState.getWorkmatesList());
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

    private void configureJoiningButtonImage() {
        joiningImageButton = findViewById(R.id.joining_image_button);
        UserManager.getInstance().getCurrentUserData().addOnSuccessListener(currentUser -> {
            if ((currentUser.getRestaurant() != null && currentUser.getRestaurant().getId() != null)
                    && currentUser.getRestaurant().getId().contains(restaurantId)) {
                joiningImageButton.setImageResource(R.drawable.ic_check_circle_green);
            } else {
                joiningImageButton.setImageResource(R.drawable.ic_check_circle_red);
            }
        });
    }

    private void configureLikeButtonImage() {
        likeImageButton = findViewById(R.id.like_image_button);
        UserManager.getInstance().getCurrentUserData().addOnSuccessListener(currentUser -> {
            if (currentUser.getFavoriteRestaurantsIdsList() == null || !currentUser.getFavoriteRestaurantsIdsList().contains(restaurantId)) {
                likeImageButton.setImageResource(R.drawable.ic_star_outline);
            } else {
                likeImageButton.setImageResource(R.drawable.ic_star_filled);
            }
        });
    }

    private void configureWorkmatesRecyclerView() {
        workmatesRecyclerView = findViewById(R.id.workmates_list_recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        workmatesRecyclerView.setLayoutManager(layoutManager);
        workmatesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        workmatesRecyclerView.setClickable(true);
    }

}
