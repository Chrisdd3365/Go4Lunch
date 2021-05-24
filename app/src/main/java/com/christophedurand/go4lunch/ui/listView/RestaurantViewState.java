package com.christophedurand.go4lunch.ui.listView;


import java.util.Objects;


class RestaurantViewState {

    private final String address;
    private final String distance;
    private final String name;
    private final String placeId;
    private final double rating;
    private final String photoURL;
    private final String openingHours;


    public RestaurantViewState(String address, String distance, String name, String placeId, double rating, String photoURL, String openingHours) {
        this.address = address;
        this.distance = distance;
        this.name = name;
        this.placeId = placeId;
        this.rating = rating;
        this.photoURL = photoURL;
        this.openingHours = openingHours;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantViewState that = (RestaurantViewState) o;
        return Double.compare(that.rating, rating) == 0 &&
                Objects.equals(address, that.address) &&
                Objects.equals(distance, that.distance) &&
                Objects.equals(name, that.name) &&
                Objects.equals(placeId, that.placeId) &&
                Objects.equals(photoURL, that.photoURL) &&
                Objects.equals(openingHours, that.openingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, distance, name, placeId, rating, photoURL, openingHours);
    }


    @Override
    public String toString() {
        return "RestaurantViewState{" +
                "address='" + address + '\'' +
                ", distance='" + distance + '\'' +
                ", name='" + name + '\'' +
                ", placeId='" + placeId + '\'' +
                ", rating=" + rating +
                ", photoURL='" + photoURL + '\'' +
                ", openingHours='" + openingHours + '\'' +
                '}';
    }

}
