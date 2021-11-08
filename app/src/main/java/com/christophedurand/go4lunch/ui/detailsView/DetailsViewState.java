package com.christophedurand.go4lunch.ui.detailsView;

import com.christophedurand.go4lunch.model.User;

import java.util.List;


public class DetailsViewState {

    private final String restaurantId;
    private final String restaurantName;
    private final String restaurantAddress;
    private final String photoURL;
    private final String phoneNumber;
    private final String websiteURL;
    private final int joiningButtonDrawableResId;
    private final int favoriteButtonDrawableResId;
    private final List<User> workmatesList;


    public DetailsViewState(String restaurantId, String restaurantName, String restaurantAddress, String photoURL, String phoneNumber, String websiteURL, int joiningButtonDrawableResId, int favoriteButtonDrawableResId, List<User> workmatesList) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.photoURL = photoURL;
        this.phoneNumber = phoneNumber;
        this.websiteURL = websiteURL;
        this.joiningButtonDrawableResId = joiningButtonDrawableResId;
        this.favoriteButtonDrawableResId = favoriteButtonDrawableResId;
        this.workmatesList = workmatesList;
    }


    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public int getJoiningButtonDrawableResId() {
        return joiningButtonDrawableResId;
    }

    public int getFavoriteButtonDrawableResId() {
        return favoriteButtonDrawableResId;
    }

    public List<User> getWorkmatesList() {
        return workmatesList;
    }

}
