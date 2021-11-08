package com.christophedurand.go4lunch.ui.listView;


public class RestaurantViewState {

    private final String address;
    private final String distance;
    private final String name;
    private final String placeId;
    private final double rating;
    private final String photoURL;
    private final String openingHours;
    private final String numberOfWorkmates;


    public RestaurantViewState(String address, String distance, String name, String placeId, double rating, String photoURL, String openingHours, String numberOfWorkmates) {
        this.address = address;
        this.distance = distance;
        this.name = name;
        this.placeId = placeId;
        this.rating = rating;
        this.photoURL = photoURL;
        this.openingHours = openingHours;
        this.numberOfWorkmates = numberOfWorkmates;
    }


    public String getAddress() {
        return address;
    }

    public String getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public double getRating() {
        return rating;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getNumberOfWorkmates() {
        return numberOfWorkmates;
    }

}
