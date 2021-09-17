package com.christophedurand.go4lunch.ui.detailsView;

import java.util.Objects;


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDetailsViewState that = (RestaurantDetailsViewState) o;
        return Objects.equals(restaurantId, that.restaurantId) && Objects.equals(restaurantName, that.restaurantName) && Objects.equals(restaurantAddress, that.restaurantAddress) && Objects.equals(photoURL, that.photoURL) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(websiteURL, that.websiteURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, restaurantName, restaurantAddress, photoURL, phoneNumber, websiteURL);
    }

    @Override
    public String toString() {
        return "RestaurantDetailsViewState{" +
                "restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", websiteURL='" + websiteURL + '\'' +
                '}';
    }

}
