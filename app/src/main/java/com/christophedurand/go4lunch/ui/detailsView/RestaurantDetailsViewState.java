package com.christophedurand.go4lunch.ui.detailsView;


public class RestaurantDetailsViewState {

    private final String restaurantId;
    private final String restaurantName;
    private final String restaurantAddress;
    private final String photoURL;
    private final String phoneNumber;
    private final String websiteURL;


    public RestaurantDetailsViewState(String restaurantId, String restaurantName, String restaurantAddress, String photoURL, String phoneNumber, String websiteURL) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.photoURL = photoURL;
        this.phoneNumber = phoneNumber;
        this.websiteURL = websiteURL;
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

}
